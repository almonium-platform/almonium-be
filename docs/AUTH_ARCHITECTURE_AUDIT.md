# Authentication Architecture and Security Audit

Review date: 2026-07-18. This document is deliberately limited to identity,
authentication, sessions, and sensitive account actions. Authorization of
domain objects such as cards is covered by `PROJECT_AUDIT.md` and still needs a
separate implementation pass.

## Executive verdict

The authentication subsystem is not a toy. It is a small identity platform
supporting local credentials, three social providers, provider linking,
reauthentication, email workflows, JWT cookies, persisted refresh-token
metadata, and account lifecycle operations. The separation between local,
OAuth, common identity, and token packages is mostly understandable.

The design should not be trusted in production unchanged. Three issues are
urgent:

1. A client cookie is decoded with native Java deserialization.
2. Access and refresh JWTs are indistinguishable, so a refresh JWT can be
   accepted as a bearer credential by the general authentication filter.
3. Refresh JWT validity does not consult the persisted refresh-token record, so
   logout/revocation is not actually enforced during refresh.

There are also account-linking, redirect, rate-limit, token-revocation, and
operational gaps. These are repairable, but maintaining them correctly is a
permanent security responsibility. Moving identity to a managed provider is a
reasonable startup decision.

## System map

```text
                         +-----------------------+
email/password --------> | Spring Authentication |
                         | Manager + BCrypt       |
                         +-----------+-----------+
                                     |
Google/Facebook/Apple --> Spring OAuth2 Login    Google One Tap token
                                     |                    |
                                     v                    v
                           OAuth2AuthenticationService
                                     |
                                     v
 UserRegistrationService <---- User + Principal identity model
                                     |
                                     v
                           AuthenticationService
                        (streak + SecurityContext)
                                     |
                                     v
                     access JWT + refresh JWT cookies
                                     |
                       TokenAuthenticationFilter
                                     |
                                     v
                         authenticated controllers

Sensitive controller --@RequireRecentLogin--> RecentLoginAspect
Verification email event --------------------> RabbitMQ/email infrastructure
```

The system is stateless from Spring Security's perspective, but it is not
fully stateless in practice: user/principal data, verification tokens, and
refresh-token JTIs are persisted in PostgreSQL.

## Identity and persistence model

### User versus Principal

`User` is the product account. A `Principal` is one way to authenticate that
account. This distinction is good and should survive any managed-auth
migration.

`Principal` uses JPA single-table inheritance:

- common fields: internal principal UUID, user, email, provider, timestamps;
- `LocalPrincipal`: BCrypt password and last password-reset date;
- `OAuth2Principal`: provider subject ID and transient profile attributes.

Supported provider values are `LOCAL`, `GOOGLE`, `FACEBOOK`, and `APPLE`. The
database uniquely constrains `(provider, provider_user_id)`, which is the right
stable identity for a social account. Product code still treats email as the
canonical account-linking key, which needs stricter policy.

### VerificationToken

Verification tokens support:

- email verification;
- password reset;
- email-change verification.

They are persisted as plaintext tokens with an expiry and token type. A
60-second resend cooldown is enforced in application code and emails are
published as events. Tokens are deleted after successful use.

### RefreshToken

The database stores JTI, user, issue/expiry times, and a `revoked` flag. JWTs
themselves contain principal/user IDs, email, provider, JTI, issue/expiry, and
an `isLive` claim. The configured lifetimes are 15 minutes for access and 14
days for refresh.

The persisted record currently looks authoritative but is not used when a
refresh request is validated. That mismatch is more dangerous than either a
proper stateful-session model or a deliberately stateless JWT model.

## Supported flows

### Local registration and login

Registration validates email uniqueness, creates the complete product user and
default plan, creates a BCrypt local principal, and sends an email-verification
token. Production requires verification before login.

Login delegates password verification to Spring Security, updates the profile
login streak, establishes the current `SecurityContext`, and emits access and
refresh cookies.

### Email verification, password reset, and email change

- Forgot-password responses avoid obvious account enumeration.
- Email verification and password reset consume typed one-time tokens.
- Email change creates a temporary local principal for the new address. After
  verification it changes the product email and unlinks principals whose email
  differs.
- Authenticated users can cancel/resend pending verification workflows.

The temporary-principal email-change design works, but it makes identity
invariants and cleanup harder than necessary. A pending-email record owned by
the user would be clearer than a second local login identity.

### Social sign-in

Spring OAuth2 Login handles Google, Facebook, and Apple authorization-code
flows. Provider response adapters normalize provider subject, email,
verification, names, and avatar. Apple has custom code exchange, JWT/JWKS
verification, and a thread-local bridge because Apple returns profile fields
differently on first sign-in.

Google One Tap separately verifies a Google ID token and feeds the same
`OAuth2AuthenticationService`.

On social sign-in the service:

1. finds an identity by `(provider, provider subject)`;
2. otherwise finds a product user by email;
3. automatically adds the provider principal to that user, or creates a new
   product user;
4. optionally copies the provider avatar;
5. issues the application's own JWT cookies.

### Link, unlink, and reauthenticate

OAuth requests carry a `SIGN_IN`, `LINK`, or `REAUTH` intent. The service can
add social/local methods, list providers, prevent removal of the final method,
and unlink a selected provider.

Sensitive endpoints are annotated with `@RequireRecentLogin`. A live login
creates an access JWT with `isLive=true`; access JWTs created through refresh
have `isLive=false`. The aspect permits sensitive operations only with the
live access-token cookie.

### Logout and account deletion

Authenticated logout deletes all persisted refresh-token records for the user
and clears cookies. A separate public logout only clears cookies. Account
deletion requires recent login, publishes cleanup information for Stripe,
avatars, Stream/Firebase-related consumers, deletes the product user, and
clears cookies.

The application-specific cleanup orchestration will remain application code
even if identity is outsourced.

## What is done well

- Product accounts are separated from authentication identities.
- Social identities use provider subject IDs instead of only email.
- Spring Security performs password authentication; passwords use BCrypt.
- Provider response normalization and shared post-login issuance reduce flow
  duplication.
- Password-reset requests do not reveal whether an email exists.
- Verification tokens are typed, expiring, one-use records with resend
  cooldowns.
- Sensitive actions have an explicit recent-login policy.
- Unlinking the last authentication method is prevented.
- Success redirects have an allowlist check.
- Cookies are Secure, HttpOnly, and SameSite=Lax; refresh-cookie path is
  narrowed.
- Apple ID tokens validate issuer, audience, signature, and provider JWKS.
- Auth tests cover important service behavior, although the threat-oriented
  matrix is incomplete.

## Security findings and remediation order

### P0: remove native Java deserialization from OAuth cookies

`CookieUtil.deserialize` runs `ObjectInputStream.readObject()` on bytes supplied
by the browser. The OAuth authorization-request cookie is not authenticated by
the application before parsing. Native Java deserialization is an unsafe input
boundary and can become code execution when a usable gadget exists on the
classpath.

Replace it immediately with one of:

- a random, one-use server-side authorization-attempt ID whose state is stored
  in Redis/PostgreSQL for three minutes; or
- a minimal JSON record protected by authenticated encryption/signing, strict
  type/size limits, expiry, and one-time consumption.

Do not serialize the Spring `OAuth2AuthorizationRequest` object graph into a
client cookie.

### P0: distinguish and constrain every token class

Both access and refresh tokens are generated by the same method with the same
issuer/key/claims. `TokenAuthenticationFilter` accepts any valid signed JWT from
the Authorization header or access cookie. Consequently, a stolen refresh JWT
can be sent as a bearer token to normal authenticated endpoints.

Add and enforce:

- `typ=access` versus `typ=refresh`;
- separate audiences, e.g. `almonium-api` and `almonium-token-endpoint`;
- issuer validation;
- separate signing keys if practical;
- endpoint-specific parsers that reject the wrong type;
- required claims and maximum allowed lifetime checks.

The refresh token must never create a normal request `SecurityContext`.

### P0: make refresh revocation and rotation effective

The refresh controller verifies only JWT signature/expiry and never loads the
JTI from `RefreshTokenRepository`. Deleting records during logout therefore
does not invalidate a copied refresh JWT. The `revoked` column is unused, and
refresh returns the same token rather than rotating it.

Use an atomic rotation transaction:

1. Parse only a refresh token and obtain its JTI.
2. Lock/load the persisted session; require matching user, active state, and
   unexpired timestamp.
3. Mark the old token consumed/revoked.
4. Issue and persist a new refresh JTI plus a new access token.
5. If an already-consumed token is replayed, revoke the token family/session
   and require login.

Hash refresh-token secrets/JTIs at rest where feasible. Add device/session ID,
last-used time, IP/user-agent metadata with a retention policy, and endpoints
to list/revoke sessions.

### P1: bind OAuth intent to the authenticated user and authorization attempt

`intent`, `userId`, and redirect cookies are independently client-controlled.
`LINK` finds the target product account by the provider email rather than by a
cryptographically authenticated current session. `REAUTH` receives a user ID
from the cookie. All intent data should be part of the same authenticated,
one-use authorization attempt and bound to:

- current product user ID for link/reauth;
- OAuth state and PKCE verifier;
- intended provider and operation;
- created/expiry time;
- exact post-login redirect identifier.

For manual linking, require a current session plus recent authentication, and
prove control of both identities. Do not make a cross-account link merely
because emails match.

### P1: define a provider-assurance account-linking policy

New social identities are automatically linked to an existing user by email.
Google and Apple expose an explicit verified-email signal. The Facebook adapter
currently returns `true` unconditionally. Email ownership and reassignment
semantics differ by provider, so automatic linking can cause account takeover.

Recommended rule:

- existing `(provider, subject)` always identifies the account;
- a new provider must not silently attach to an existing account;
- require login to the existing account and fresh authentication of the new
  provider, or a carefully reviewed trusted-provider policy;
- never treat a hard-coded `emailVerified=true` as evidence;
- preserve an immutable internal user ID when primary email changes.

### P1: validate redirects on both success and failure

The success handler checks host and port but not scheme or exact registered
redirect. The failure handler redirects directly to the cookie value without
calling the allowlist check and appends the localized exception message.

Use server-side redirect IDs or exact normalized URI matching including scheme,
host, port, and permitted path. On failure, return a stable public error code,
not internal exception text.

### P1: revoke sessions after credential/account changes

Password reset, password change, email change, unlinking the currently used
provider, and account security recovery do not consistently revoke live refresh
sessions. Revoke/rotate all relevant sessions and clear cookies after these
operations. Decide explicitly whether a password change keeps the current
session and revokes all others.

### P1: add abuse controls

Add per-IP and per-account/email limits for login, registration, verification,
resend, forgot-password, Google One Tap, OAuth starts/callback failures, and
refresh. Use progressive delay or temporary lockout without allowing attackers
to permanently lock victims out. Add CAPTCHA/risk checks only after thresholds,
not on every healthy request.

Review CSRF boundaries at the same time. All `/public/**` endpoints are exempt,
including cookie-consuming refresh and logout paths. SameSite=Lax helps in
modern browsers but should not be the only intentional defense for login,
refresh, logout, or account-link initiation. Use explicit request matchers and
CSRF/state protections appropriate to each flow.

### P1: account state must affect authentication

`LocalPrincipal` currently reports accounts as always enabled, unlocked, and
non-expired. JWT request authentication reconstructs identity entirely from
claims without checking whether the user/principal still exists or is disabled.
Introduce explicit user/principal status and a strategy for near-real-time
disablement: short access tokens plus authoritative refresh checks, a token
version/revoked-before timestamp, or a cached status check for sensitive paths.

### P2: verification-token hardening

- Store a cryptographic hash of verification tokens, not the bearer secret.
- Add a unique constraint/index to the token hash.
- Consume tokens atomically to prevent concurrent reuse.
- Record attempt counts and rate-limit validation endpoints.
- Use a CSPRNG with adequate entropy for every externally reachable token.
- Keep a separate pending-email model instead of a temporary local principal.

### P2: key and claim management

One symmetric JWT secret signs everything. Add issuer/audience/type validation,
key IDs, rotation with overlapping verification keys, secret-manager storage,
clock-skew policy, and emergency revocation procedure. Prefer asymmetric keys
when multiple services or consumers verify tokens.

Token parsing should map every JJWT validation failure—including bad signatures
and invalid claims—to a quiet 401 without a stack trace. The current catch list
is narrower than the parser's failure surface, and logging full exceptions for
attacker-controlled invalid tokens enables log flooding.

Avoid logging raw JWTs, OAuth codes, verification tokens, passwords, provider
responses, cookie values, or sensitive query strings. Reduce security/web debug
logging in production as documented in the project audit.

### P2: Apple/OAuth client robustness

The custom Apple JWKS fetch uses `HttpURLConnection` without explicit connect
or read timeouts and caches keys indefinitely by `kid`. Use a maintained OIDC
decoder/client where possible, honor HTTP cache headers, time out requests,
refresh on unknown/rotated keys, limit response size, and expose metrics.

Review nonce and PKCE handling for every provider. Apple first-login profile
JSON comes directly from the request parameter; use it only as optional display
data after the independently verified ID token establishes identity.

### P2: cleanup and consistency

- Use one canonical logout endpoint; make public logout explicitly local-only.
- Make email comparisons normalized and case rules explicit.
- Check new-email availability transactionally in every email-change/link flow
  and rely on a database uniqueness constraint as the final authority.
- Add database uniqueness constraints for the intended number of local
  principals and verification workflows per user.
- Replace `@SneakyThrows` and generic authentication errors with stable internal
  error categories and non-sensitive public responses.
- Remove the redundant custom password-encoder subclass unless it carries
  intentional cost/upgrade configuration.
- Decide whether a user may have several sessions; the entity says one-to-one
  while the database migration does not enforce a unique user constraint.

## Missing tests

Add a threat-oriented matrix before changing behavior:

- refresh token sent as API bearer/access cookie;
- access token sent to refresh endpoint;
- refresh after logout, password reset/change, provider unlink, user disable,
  and account deletion;
- refresh rotation replay and concurrent refresh;
- OAuth state/PKCE/nonce tampering and expired authorization attempt;
- modified intent, user ID, redirect, and authorization-request cookies;
- failure-handler open redirects;
- linking when provider emails differ, are unverified, or collide;
- linking a provider already attached to another user;
- removal of current/last login method;
- concurrent use of verification token;
- brute-force/rate-limit boundaries;
- signing-key rotation and old-key retirement;
- CSRF behavior for every cookie-authenticated state-changing endpoint.

Tests should assert security outcomes rather than only service calls. Use real
Spring Security filters and cookie/CSRF behavior for the highest-risk paths.

## Managed replacement analysis

No identity vendor completely replaces all code in this package. A provider can
replace credential storage, social OAuth, email verification/reset, identity
linking, MFA, token/session issuance, and recent-login evidence. Almonium must
still own:

- the product `User`, profile, learner, plan, and billing records;
- mapping the provider's immutable user ID to the Almonium UUID;
- login streak and onboarding side effects;
- avatar import policy;
- authorization to cards and other domain objects;
- Stripe/Stream/Firebase/avatar cleanup during account deletion;
- business policy around the final login method and email changes.

### Recommendation: Firebase Authentication

For this repository, Firebase Authentication is the most pragmatic replacement
of the current feature set at the lowest integration cost:

- email/password, email verification, password reset, Google, Facebook, and
  Apple are supported;
- client SDKs support link/unlink and credential reauthentication;
- sensitive operations already require recent sign-in, and tokens expose an
  `auth_time` claim that the Java backend can enforce;
- refresh-token revocation is managed, and password resets/major account
  changes invalidate sessions;
- Java Admin SDK verifies ID tokens, creates/verifies HttpOnly session cookies,
  manages users, and revokes tokens;
- BCrypt password hashes can be imported, so local users need not reset their
  passwords;
- Firebase Admin is already a project dependency and Firebase is already used
  elsewhere in Almonium.

Official references:

- [Firebase Authentication overview and pricing](https://firebase.google.com/docs/auth)
- [Firebase pricing](https://firebase.google.com/pricing)
- [Web user management, reset, and recent reauthentication](https://firebase.google.com/docs/auth/web/manage-users)
- [Provider account linking and unlinking](https://firebase.google.com/docs/auth/web/account-linking)
- [Java session-cookie management and `auth_time`](https://firebase.google.com/docs/auth/admin/manage-cookies)
- [Session revocation](https://firebase.google.com/docs/auth/admin/manage-sessions)
- [BCrypt user import](https://firebase.google.com/docs/auth/admin/import-users)

Cost caveat: standard Firebase Authentication has broad no-cost usage for most
non-phone methods. If the project is upgraded to Identity Platform, the current
published allowance is 50,000 MAU on Blaze (and a 3,000 DAU limit for most
providers on upgraded Spark projects), followed by usage pricing. Verify the
exact project mode and regional pricing before migration.

Firebase is not a perfect managed-device-session product. Its standard
revocation primitive is primarily per user rather than a rich application-level
device/session inventory. If Almonium needs "show every device and revoke one"
as a core feature, retain a small application session table keyed to Firebase
session IDs or choose Clerk.

### Closest feature-rich alternative: Clerk

Clerk is the strongest option if the priority is eliminating UI and session
management work, not minimizing backend integration changes. Its current Hobby
plan includes 50,000 retained users and up to three social connections—exactly
enough for Google, Facebook, and Apple. It provides automatic account-linking
protections, external-account management, active sessions, prebuilt profile UI,
and explicit reverification for sensitive operations. BCrypt migration is
supported.

Official references:

- [Clerk pricing](https://clerk.com/pricing)
- [Sensitive-action reverification](https://clerk.com/docs/guides/secure/reverification)
- [OAuth account linking](https://clerk.com/docs/guides/configure/auth-strategies/social-connections/account-linking)
- [User and external-account management](https://clerk.com/docs/guides/users/managing)
- [Migration and password hashes](https://clerk.com/docs/guides/development/migrating/overview)
- [Manual JWT verification](https://clerk.com/docs/guides/sessions/manual-jwt-verification)

Tradeoffs for Almonium:

- there is no first-class Java/Spring SDK in the reviewed documentation; Spring
  should verify Clerk JWTs through its JWKS/resource-server support and call the
  Backend API where necessary;
- ClerkJS is framework-agnostic but the frontend integration is a larger change
  than using Firebase's existing ecosystem;
- the free plan has product limitations such as branding/fixed session policy,
  and some device-security features require paid production plans;
- Clerk's short-lived `__session` cookie is JavaScript-readable by design,
  whereas the current Almonium cookies are HttpOnly. Understand this security
  model before adopting it.

### Other credible options

| Provider | Current entry tier | Strength here | Main reservation |
| --- | --- | --- | --- |
| Auth0 | Free up to 25,000 MAU | Most standards-oriented Spring/OIDC option; refresh rotation, broad social support, mature migration | Paid tiers climb quickly; some account-linking capabilities vary by plan |
| Supabase Auth | Free 50,000 MAU; Pro from $25/month | Open-source/self-hostable, all required social providers, custom SMTP, basic MFA | Free projects pause; richer session controls are paid; weaker Java/Spring ergonomics |
| Keycloak | Software is free/self-hosted | Full control, OIDC standards, no per-user vendor fee | You remain the identity operator: patching, email delivery, backups, abuse protection, and uptime are yours |

References: [Auth0 pricing](https://auth0.com/pricing),
[Auth0 refresh rotation](https://auth0.com/docs/secure/tokens/refresh-tokens/refresh-token-rotation),
[Auth0 account linking](https://auth0.com/docs/manage-users/user-accounts/user-account-linking),
and [Supabase pricing](https://supabase.com/pricing).

## Migration shape if Firebase is chosen

Do not rewrite everything at once. Use an external identity mapping and a
dual-verification period:

1. Add immutable `firebase_uid` (or a generic `external_identity_id`) to the
   Almonium user, unique and nullable during migration.
2. Export users/principals. Import local identities with existing BCrypt hashes
   and verified-email flags. Recreate/link social identities carefully; test
   provider-subject preservation and collision behavior before production.
3. Add a Spring filter that verifies Firebase ID tokens or Firebase session
   cookies and resolves `firebase_uid -> User`.
4. Prefer the documented Firebase HttpOnly session-cookie exchange if the
   current browser-cookie architecture should remain. Check `auth_time` before
   issuing the cookie and on Almonium-sensitive endpoints.
5. Move registration/login/reset/verification/linking UI to Firebase SDK flows.
   Keep a small authenticated backend endpoint that creates Almonium product
   records idempotently after first identity creation.
6. Replace local account-deletion authentication with Firebase deletion, but
   keep the existing Almonium deletion event/outbox workflow for business data.
7. Run old and new authentication behind a feature flag for migrated cohorts.
   Record identity-link conflicts for manual review; never silently merge them.
8. After all active sessions and users have migrated, remove password,
   verification-token, OAuth cookie, Apple custom exchange, and JWT issuance
   code in separate commits.

Before committing to a vendor, build a one-day spike proving these exact cases:
Google, Facebook, Apple, password import/login, email verification/reset,
link/unlink, recent-login enforcement in Spring, HttpOnly cookie refresh,
logout/revocation, and account deletion. The spike should decide between
Firebase's lower integration/cost burden and Clerk's richer managed session UX.

## If custom auth is retained

The minimum safe sequence is:

1. Remove OAuth Java deserialization.
2. Introduce strict token types/audiences/issuers and prevent refresh-as-access.
3. Implement persisted refresh rotation, replay detection, and effective
   logout/revocation.
4. Bind OAuth state/intents/redirects to a one-use server-side attempt.
5. Replace automatic email linking with proof of both identities.
6. Revoke sessions after security changes and add account status checks.
7. Add rate limits, token hashing, key rotation, and the threat-oriented tests.
8. Only then add MFA, passkeys, session/device UI, recovery codes, anomaly
   detection, and breached-password protection.

Those "perks" are precisely where a managed identity provider provides the
largest long-term value. Rebuilding them is educational, but it is not the
highest-leverage startup work unless identity itself is the product.
