<p align="center">
  <img src="src/main/resources/static/logo.png" alt="Almonium logo.png"/>
  <img src="src/main/resources/static/title-white.png" alt="Almonium title.png"/>
</p>

[Try online](https://almonium.com)

[Frontend project](https://github.com/okuzan/almonium-fe)

[Mobile project](https://github.com/okuzan/almonium-mobile)

[Infrastructure project](https://github.com/okuzan/almonium-infra)

[Chrome extension](https://github.com/okuzan/chrome-extension)

[API Reference (Swagger UI)](https://api.almonium.com/api/v1/swagger-ui/index.html)

The Angular frontend uses the browser session-cookie flow. The Expo/React
Native mobile client uses Firebase ID-token bearer authentication and is
distributed as native apps; both clients consume this API and must be considered
when changing DTOs, errors, authorization, or user flows.

## Development build

Use JDK 21 and the checked-in Maven wrapper so local development and CI use the
same Maven version:

```bash
./mvnw clean compile -DskipTests
./mvnw verify
```

On Windows, use `mvnw.cmd` instead. The full verification suite uses
Testcontainers, so Docker must be running and accessible to the current user.
Spotless checks formatting during the build; apply intentional formatting with
`./mvnw spotless:apply`.

See the [development-environment runbook](docs/LOCAL_DEVELOPMENT.md) for the
local PostgreSQL/RabbitMQ startup command, application startup,
troubleshooting, and the complete environment-variable catalogue.

See the [Short.io link guide](docs/SHORT_LINKS.md) for the `go.almonium.com`
public-link contract, current aliases, and safe usage boundaries.

See the [email-address list](docs/EMAIL_ADDRESSES.md) for every alias on
`almonium.com`, what each one is for, and where it is already used.

## Email template previews

Render every live friendship and subscription email through the real
Thymeleaf/CSS-inlining pipeline with:

```bash
./mvnw test -Dtest=EmailTemplatePreviewGenerator
```

The generator is a dry run: it does not send mail or require the application
to be running. Open `temp/previews/index.html` in a browser after the command
to inspect the generated stubs. The `temp/` directory is gitignored, so rerun
the command whenever a template or email fragment changes.

## Operator scripts

`scripts/` holds one-off Python utilities that sit outside the Java build —
they are not part of `./mvnw` and are run manually, on demand. This is a
separate toolchain from the Spring Boot app; run these commands from the
`scripts/` directory using a virtualenv so the dependency doesn't leak into
your system Python install:

```bash
cd scripts
python3 -m venv .venv
source .venv/bin/activate          # Windows: .venv\Scripts\activate
pip install -r requirements.txt

python set_firebase_admin_claim.py you@example.com

deactivate
```

`.venv/` and `__pycache__/` under `scripts/` are gitignored; `requirements.txt`
is the only file that needs to be committed when a script's dependencies
change.

`set_firebase_admin_claim.py` grants or revokes the Firebase `admin` custom
claim, which is the only source of `ROLE_ADMIN` in this backend (see
`FirebaseSessionService`/`SecurityRoles`) — nothing in Postgres represents
adminhood. It reads the same service-account credential the app itself uses
locally (`GOOGLE_SERVICE_ACCOUNT_KEY_BASE64` in `.env`), or accepts
`--key-file path/to/service-account.json` to target a different Firebase
project (e.g. prod) without touching your local `.env`. Run it once per
Firebase project, not once per local Docker reset — the claim lives in
Firebase, not the disposable local database.

# Friendships

## Scenarios

#### Everywhere:

- Both User A and User B must be different.
- At most one relationship row may exist for an unordered user pair. In other
  words, `(A, B)` and `(B, A)` identify the same relationship.
- Relationship updates use optimistic locking. Concurrent actions against a
  stale relationship, and concurrent attempts to create the same pair, return
  a conflict response instead of silently overwriting state.

### Sending a Friend Request

**Flow**: User A sends a friend request to User B.

**Status**: `PENDING`

**Checks**: User B should not have blocked friend requests.

### Accepting a Friend Request

**Flow**: User B accepts the friend request from User A.

**Status**: `FRIENDS`

**Checks**: The existing status must be `PENDING`.

### Rejecting a Friend Request

**Flow**: User B rejects the friend request from User A.

**Status**: `REJECTED` (retained so a later request can re-establish the relationship).

**Checks**: The existing status must be `PENDING`.

### Canceling a Friend Request

**Flow**: User A cancels the friend request sent to User B.

**Status**: `CANCELLED` (retained so a later request can re-establish the relationship).

**Checks**: The existing status must be `PENDING`.

### Blocking a User

**Flow**: User A blocks User B.

**Status**: `FST_BLOCKED_SND` or `SND_BLOCKED_FST` depending on who initiated the action.

**Checks**: The same user cannot block twice. If the other user has already
blocked, the status becomes `MUTUAL_BLOCK`.

**Note**: Blocking prevents the blocked user from sending friend requests.

### Unblocking a User

**Flow**: User A unblocks User B.

**Status**: `UNFRIENDED` after a unilateral block is removed. Removing one side
of a mutual block preserves the other user's one-sided block.

**Checks**: Only a user who currently blocks the other user can unblock.

### Unfriending a User

**Flow**: User A unfriends User B.

**Status**: `UNFRIENDED` (retained so a later request can re-establish the relationship).

**Checks**: The existing status must be `FRIENDS`.

## Enums

### Friendship Statuses

- `FRIENDS`: Both users are friends.
- `PENDING`: A friend request has been sent but not yet accepted.
- `FST_BLOCKED_SND`: The first user has blocked the second user.
- `SND_BLOCKED_FST`: The second user has blocked the first user.
- `MUTUAL_BLOCK`: Both users have blocked each other.
- `REJECTED`: The recipient rejected a request; the relationship may be retried.
- `CANCELLED`: The requester canceled a request; the relationship may be retried.
- `UNFRIENDED`: A friendship or unilateral block ended; the relationship may be retried.

### Friendship Actions

- `REQUEST`: Send a friend request.
- `ACCEPT`: Accept a friend request.
- `REJECT`: Reject a friend request.
- `CANCEL`: Cancel an outgoing friend request.
- `BLOCK`: Block a user.
- `UNBLOCK`: Unblock a user.
- `UNFRIEND`: Remove a user from the friend list.

Relationship list responses expose status relative to the authenticated user:
`PENDING_OUTGOING`, `PENDING_INCOMING`, `FRIENDS`, or `BLOCKED`. The positional
`FST_BLOCKED_SND` and `SND_BLOCKED_FST` values remain persistence details.

IDEA Setup.
Imports:
static
<empty line>
non-static

Plugins:
palantir-java-format
Editor -> General -> Console -> Use soft wraps in console
Editor -> Code Style -> Java -> Imports
