package com.almonium.user.friendship.model.projection;

/**
 * A projection interface for retrieving basic friend information.
 *
 * <p>This interface is used to fetch only the necessary fields (ID, username, and email)
 * of a friend from the database without loading the entire User entity.</p>
 */
public interface UserToFriendProjection {
    long getId();

    String getUsername();

    String getEmail();
}
