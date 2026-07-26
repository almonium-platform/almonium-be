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
