package com.almonium.user.friendship.repository;

import com.almonium.user.friendship.dto.response.PublicUserProfile;
import com.almonium.user.friendship.dto.response.RelatedUserProfile;
import com.almonium.user.friendship.model.entity.Friendship;
import com.almonium.user.friendship.model.projection.FriendshipToUserProjection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface FriendshipRepository extends JpaRepository<Friendship, Long> {
    @Query(
            """
            select new com.almonium.user.friendship.dto.response.PublicUserProfile(
                u.id,
                u.username,
                case when p.hidden = true then null else p.avatarUrl end
            )
            from User u
            left join u.profile p
            where u.username like CONCAT('%', :username, '%')
              and u.id != :currentUserId
              and not exists (
                  select 1
                  from Friendship f
                  where (f.requester.id = :currentUserId and f.requestee.id = u.id)
                     or (f.requestee.id = :currentUserId and f.requester.id = u.id)
              )
            """)
    List<PublicUserProfile> findNewFriendCandidates(long currentUserId, String username);

    @Query(
            """
            select new com.almonium.user.friendship.dto.response.RelatedUserProfile(
                f.requestee.id,
                f.requestee.username,
                case when p.hidden = true then null else p.avatarUrl end,
                f.id,
                f.status
            )
            from User u
            join Friendship f on f.requester.id = u.id
            join Profile p on f.requestee.id = p.id
            where f.requester.id = :id and f.status = 'PENDING'
            """)
    List<RelatedUserProfile> getSentRequests(long id);

    @Query(
            """
            select new com.almonium.user.friendship.dto.response.RelatedUserProfile(
                f.requester.id,
                f.requester.username,
                case when p.hidden = true then null else p.avatarUrl end,
                f.id,
                f.status
            )
            from User u
            join Friendship f on f.requestee.id = u.id
            join Profile p on f.requester.id = p.id
            where f.requestee.id = :id and f.status = 'PENDING'
            """)
    List<RelatedUserProfile> getReceivedRequests(long id);

    @Query(
            """
        select new com.almonium.user.friendship.dto.response.RelatedUserProfile(
            u.id,
            u.username,
            case when p.hidden = true then null else p.avatarUrl end,
            f.id,
            f.status
        )
        from Friendship f
        join User u on
            (f.requester.id = u.id and f.status = 'SND_BLOCKED_FST' and f.requestee.id = :id)
            or
            (f.requestee.id = u.id and f.status = 'FST_BLOCKED_SND' and f.requester.id = :id)
        join Profile p on u.id = p.id
        """)
    List<RelatedUserProfile> getBlocked(long id);

    @Query(
            """
            select f from Friendship f
            where (f.requester.id = :id1 and f.requestee.id = :id2)
            or (f.requester.id = :id2 and f.requestee.id = :id1)
            """)
    Optional<Friendship> getFriendshipByUsersIds(long id1, long id2);

    /**
     * Retrieves a list of visible friendships for a given user.
     *
     * <p>This method performs a query on the Friendship table, returning a list of FriendshipToUserProjection objects,
     * which are projections that include the user's ID, the status of the friendship, and a boolean indicating
     * whether the user is the requester of the friendship.</p>
     *
     * <p>The query filters out friendships where the current user has been blocked by the other user or the status
     * is MUTUALLY_BLOCKED. Specifically, it excludes records where:</p>
     * <ul>
     *   <li>The current user is the requester and the status is SND_BLOCKED_FST</li>
     *   <li>The current user is the requestee and the status is FST_BLOCKED_SND</li>
     *   <li>The status is MUTUALLY_BLOCKED</li>
     * </ul>
     *
     * @param id The ID of the user for whom to retrieve visible friendships.
     * @return A list of FriendshipToUserProjection objects representing the visible friendships for the given user.
     */
    @Query(
            """
            select new com.almonium.user.friendship.model.projection.FriendshipToUserProjection(
                        case when f.requester.id = :id then f.requestee.id else f.requester.id end,
                        str(f.status),
                        case when f.requester.id = :id then true else false end)
                    from Friendship f
                    where (f.requester.id = :id or f.requestee.id = :id)
            and f.status = 'FRIENDS'
            """)
    List<FriendshipToUserProjection> getVisibleFriendships(long id); // TODO avoid writing FQN in query

    @Query(
            """
        select new com.almonium.user.friendship.dto.response.RelatedUserProfile(
            case
                when f.requester.id = :id then f.requestee.id
                else f.requester.id
            end,
            case
                when f.requester.id = :id then f.requestee.username
                else f.requester.username
            end,
            case
                when f.requester.id = :id then f.requestee.profile.avatarUrl
                else f.requester.profile.avatarUrl
            end,
            f.id,
            f.status
        )
        from Friendship f
        where (f.requester.id = :id or f.requestee.id = :id) and f.status = 'FRIENDS'
        """)
    List<RelatedUserProfile> getFriendships(long id);

    @Query(
            """
            select new com.almonium.user.friendship.model.projection.FriendshipToUserProjection(
                case when f.requester.id = :id then f.requestee.id else f.requester.id end,
                str(f.status),
                case when f.requester.id = :id then true else false end)
            from Friendship f
            join User u on (f.requester.id = u.id or f.requestee.id = u.id)
            where (f.requester.id = :id or f.requestee.id = :id)
              and f.status = 'FRIENDS'
              and u.username like CONCAT('%', :username, '%')
            """)
    List<FriendshipToUserProjection> searchFriendsByUsername(long id, String username);
}
