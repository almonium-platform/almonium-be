<p align="center">
  <img src="src/main/resources/static/logo.png" alt="Almonium logo.png"/>
  <img src="src/main/resources/static/title-white.png" alt="Almonium title.png"/>
</p>

[Try online](https://almonium.com)

[Frontend project](https://github.com/okuzan/almonium-fe)

[Chrome extension](https://github.com/okuzan/chrome-extension)

[API Reference (Swagger UI)](https://api.almonium.com/api/v1/swagger-ui/index.html)

# Friendships

## Scenarios

#### Everywhere:

- Both User A and User B must be different.

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

**Status**: No relationship record.

**Checks**: The existing status must be `PENDING`.

### Canceling a Friend Request

**Flow**: User A cancels the friend request sent to User B.

**Status**: No relationship record.

**Checks**: The existing status must be `PENDING`.

### Blocking a User

**Flow**: User A blocks User B.

**Status**: `FST_BLOCKED_SND` or `SND_BLOCKED_FST` depending on who initiated the action.

**Checks**:

- The existing status must be either `FRIENDS` or `PENDING`.

**Note**: Blocking prevents the blocked user from sending friend requests.

### Unblocking a User

**Flow**: User A unblocks User B.

**Status**: No relationship record.

**Checks**: The existing status must be either `FST_BLOCKED_SND` or `SND_BLOCKED_FST`.

### Unfriending a User

**Flow**: User A unfriends User B.

**Status**: No relationship record.

**Checks**: The existing status must be `FRIENDS`.

## Enums

### Friendship Statuses

- `FRIENDS`: Both users are friends.
- `PENDING`: A friend request has been sent but not yet accepted.
- `FST_BLOCKED_SND`: The first user has blocked the second user.
- `SND_BLOCKED_FST`: The second user has blocked the first user.

### Friendship Actions

- `REQUEST`: Send a friend request.
- `ACCEPT`: Accept a friend request.
- `REJECT`: Reject a friend request.
- `CANCEL`: Cancel an outgoing friend request.
- `BLOCK`: Block a user.
- `UNBLOCK`: Unblock a user.
- `UNFRIEND`: Remove a user from the friend list.

IDEA Setup.
Imports:
static
<empty line>
non-static

Plugins:
palantir-java-format
Editor -> General -> Console -> Use soft wraps in console
Editor -> Code Style -> Java -> Imports
