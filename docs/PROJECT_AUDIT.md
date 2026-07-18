# Almonium Backend: Architecture and Restart Audit

Review date: 2026-07-18. This is a point-in-time engineering handoff, intended
to make it easier to return to the project after a long break. Recheck version
numbers and external-provider status before acting on upgrade advice.

## Executive assessment

Almonium is a medium-sized, real product backend rather than a prototype. It is
a Spring Boot modular monolith with roughly 400 production Java/Kotlin source
files, about 100 HTTP endpoints, 30+ controllers and entities, 38-ish database
migrations, and a meaningful integration-test suite. The scope includes a
custom identity platform, a social graph, language-learning content, billing,
notifications, chat, and several language APIs.

The code is generally conscientious: domain packages are recognizable,
database changes are versioned, DTO mapping is usually explicit, tests cover
important relationship and Firebase-session flows, and the event/outbox work is
more mature than is typical for a side project. It does not need a rewrite.

Do not immediately resume feature development, though. Spend a short
stabilization cycle on security, build reproducibility, and production
configuration. The most important findings are correctness and operational
risks around existing features, not missing architecture layers.

## What the product appears to be

Almonium is a social language-learning system. Users create identities through
local or social authentication, complete a language-learning profile, collect
and share vocabulary cards, analyze words and phrases through dictionary and
translation services, learn from books, connect through friendships/chat, and
subscribe to paid plans.

The backend currently contains these broad areas:

| Area | Responsibilities | Approximate production footprint |
| --- | --- | ---: |
| Authentication | Firebase ID-token exchange, HttpOnly sessions, UID mapping and recent-login enforcement | 5% |
| User domain | Profile, onboarding/setup, languages, interests, avatars, relationships and privacy | 19% |
| Infrastructure/integrations | Email, events, messaging, storage, external clients and utilities | 15% |
| Analyzer | Dictionaries, translation, word frequency, NLP and TTS | 14% |
| Configuration | Security, OpenAPI, persistence, messaging and provider configuration | 9% |
| Subscription | Stripe checkout, plans and webhook processing | 7% |
| Cards | Vocabulary cards, translations, examples, sharing and suggestions | 7% |
| Learning/content | Books, progress and related learning features | 7% |

These percentages are directional package/line estimates, not product-value
estimates. Authentication and integration maintenance consume more complexity
than their visible feature surface suggests.

## Architectural shape

The project is best described as a package-oriented modular monolith:

- Spring MVC provides most HTTP endpoints; WebFlux is also present for some
  client work.
- Spring Data JPA and PostgreSQL model the durable domain.
- Liquibase owns schema and seed evolution.
- Spring Security and Firebase Admin verify managed identities and HttpOnly
  sessions.
- Spring Modulith persists application events and externalizes selected events
  to RabbitMQ, giving the project an outbox-like reliability boundary.
- MapStruct and Lombok remove mapping/boilerplate code.
- Java is the main implementation language; Kotlin is used selectively.

The package boundaries are useful but not hard module boundaries. Application
services sometimes know details that belong in provider adapters, and a few
large services combine orchestration, policy, persistence, and integration
logic. Refactor those seams incrementally; splitting the repository into
microservices would add operational cost without solving the current risks.

## Fix-first findings

### 1. Enforce card ownership and access policy

Card operations appear able to load, update, or delete records by supplied ID
without consistently proving that the authenticated user owns the card. Related
translation/example IDs and card suggestions need the same review. This is an
IDOR-style authorization risk: authentication alone does not establish access
to a particular object.

The preferred repair is an explicit user-scoped query or policy boundary, for
example `findByIdAndOwnerId`, followed by integration tests in which user A
cannot read or mutate user B's data. Apply the rule to nested objects too.

### 2. ~~Remove native Java deserialization from OAuth cookies~~ Remediated

The OAuth request cookie path uses Java object deserialization. A client-held
cookie is untrusted input, and native deserialization has a long history of
gadget-based attacks. Signing/encryption does not make an unsafe parser a good
boundary, and an unsigned or incorrectly verified cookie makes this critical.

Store a server-side opaque handle, or serialize a deliberately small JSON DTO
with authenticated encryption/signing and strict field validation. Do not
deserialize arbitrary object graphs from a request cookie.

### 3. ~~Make refresh-token revocation real~~ Removed

The custom JWT and refresh-token implementation was removed during the Firebase
Authentication migration. Firebase owns token refresh and revocation;
Almonium verifies managed session cookies and checks revocation for sensitive
actions.

### 4. Protect paid or abusable public integrations

`/public/chat` appears to accept arbitrary prompts and forward them to a paid AI
provider. Authentication endpoints and other expensive integrations also need
throttling. Add authentication where appropriate, per-user/IP quotas, input and
output limits, timeouts, cost telemetry, and abuse-safe error handling.

### 5. Correct production logging defaults

Base configuration enables verbose Spring Security/web and SQL/bind logging.
This can leak credentials, tokens, personal data, and query values while also
creating substantial log volume. Make production conservative and opt into
debug/SQL logging only in a local profile.

## Build and IDE health

The IntelliJ warnings are not evidence that Java/Kotlin interoperation is
fundamentally wrong. The initial build review found several conflicting Maven
and IDE signals. The 2026-07-18 stabilization repaired the reproducibility
issues:

- The Maven wrapper and its metadata use LF, `mvnw` is executable in Git, and
  `mvnw.cmd` retains CRLF for Windows.
- A clean production compile succeeds through the pinned Maven wrapper.
- Test compilation initially failed because learner fixtures passed `List`
  values where the production API expects `Set` values. This review repaired
  those fixtures, and test compilation now passes.
- Java and Kotlin both use the standard `java.version` property and target JVM
  21.
- Kotlin sources live under `src/main/java`, which can work but is surprising
  to IDEs and contributors; either document it or migrate them together to
  `src/main/kotlin`.
- CI runs `./mvnw -B verify`; integration tests require a Docker-capable runner.
- Spotless checks formatting during `validate` and only mutates files when
  `spotless:apply` is invoked explicitly.
- Review workflow checkout/deploy conditions so builds test the intended SHA
  and deployment does not run after an invalid verification path.

The remaining build work is to confirm the full Testcontainers suite in CI and
review workflow checkout/deploy conditions. These repairs should clear most
module/IDE symptoms before any source-set redesign is needed.

## Authentication assessment

Firebase Authentication now owns credentials, verification/reset emails,
Google sign-in, provider linking, and token issuance. Almonium maps Firebase UID
to its product user, exchanges recent ID tokens for HttpOnly cookies, performs
local signature verification on ordinary requests, and performs revocation-aware
verification for sensitive actions. Email collisions are rejected and never
used to merge identities. Remaining review areas are cookie/CSRF behavior,
Firebase IAM, session-lifetime policy, account-deletion failure handling, and
object-level authorization.

## Technology and integration inventory

Core technologies include Java 21, Kotlin, Spring Boot 3.4, Spring MVC/WebFlux,
Spring Security, Firebase Admin, Spring Data JPA, PostgreSQL, Liquibase, RabbitMQ, Spring
Modulith, Maven, JUnit/Mockito/Testcontainers, Lombok, and MapStruct.

External systems visible in the code/dependencies include:

- Stripe for plans, checkout, subscriptions, invoices, and webhooks;
- Google Translate and Text-to-Speech;
- Yandex dictionary/translation;
- Wordnik, WordsAPI, Free Dictionary, Urban Dictionary, and Datamuse;
- OpenAI and Google Gemini-style AI endpoints;
- Firebase push notifications;
- Stream Chat;
- email delivery (including ZeptoMail);
- object/static storage and application messaging infrastructure.

Each outbound client should have explicit connect/read timeouts, bounded
retries only where safe, observability, rate-limit handling, and a provider
adapter that prevents transport DTOs from spreading through the domain.

One NLP service currently returns `null`, while Stanford CoreNLP and large model
artifacts add hundreds of megabytes. Either finish and test that capability or
remove/lazily isolate the dependency until it has a user-facing path.

## Dependency strategy

Do not perform one giant dependency upgrade. Establish a green build first,
then use grouped updates:

1. Upgrade within the current Spring Boot line and align its managed Spring
   Modulith/springdoc ecosystem.
2. Update patch/minor versions of provider SDKs with contract tests.
3. Upgrade MapStruct and build plugins, then remove redundant explicit versions
   already managed by Boot/BOMs.
4. Evaluate the next Spring Boot major only after deprecations and tests are
   clean.

Provider models deserve separate attention. The hard-coded Gemini 1.5 model
and GPT-3.5-era raw chat integration are likely obsolete by the review date.
Resolve model names from configuration, expose capability/cost limits, and use
the provider's current supported API rather than baking a model into domain
code.

## Glaring omissions and worthwhile additions

Prioritized additions:

1. Object-level authorization integration tests.
2. A reproducible `verify` build in CI, including Testcontainers where needed.
3. Rate limiting and cost/abuse controls.
4. External-client timeouts, retry policy, metrics, and failure mapping.
5. Security-focused refresh/OAuth/account-linking tests.
6. Dependency and container scanning plus automated update PRs.
7. Production-ready health/readiness checks and integration telemetry.
8. API pagination and hard result-size limits where collections can grow.
9. Architecture tests or Spring Modulith verification to keep package
   dependencies intentional.
10. A short local-development runbook and environment-variable catalogue.

## How AI can help this product

Use AI behind bounded product capabilities rather than as an unrestricted
general chat proxy. Strong candidates are:

- explain a saved word in the learner's level and native language;
- generate example sentences constrained to known vocabulary/CEFR level;
- turn a book passage into reviewed candidate cards;
- produce distractors, cloze exercises, and spaced-repetition variations;
- normalize/merge results from dictionary providers;
- provide semantic search and duplicate-card detection;
- evaluate an answer against a rubric while preserving deterministic scoring
  and an appeal path.

Keep deterministic storage, permissions, billing, and workflow rules outside
the model. Add structured outputs, validation, prompt/version tracking, cost
budgets, caching, safety controls, and an evaluation dataset before making an
AI feature core to learning progress.

AI is also useful in development for generating authorization test matrices,
provider contract fixtures, migration reviews, documentation, and mechanical
refactors. Human review remains essential around auth, payments, migrations,
and learner-facing factual content.

## Recommended restart sequence

1. Repair wrapper, JVM target alignment, test compilation, and CI verification.
2. Fix card ownership/access checks and add cross-user integration tests.
3. replace OAuth native cookie deserialization and repair refresh-token
   revocation/rotation.
4. Move sensitive debug logging out of production and protect paid endpoints.
5. Add external-client timeouts/telemetry and remove or isolate dead heavy code.
6. Upgrade dependencies in small, tested groups.
7. Re-read the domain tests and resume product features from a stable baseline.

That stabilization is finite. Once these items are controlled, the existing
modular monolith is a reasonable foundation for continued feature development.
