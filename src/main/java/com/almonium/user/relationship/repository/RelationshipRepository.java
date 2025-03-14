package com.almonium.user.relationship.repository;

import com.almonium.user.relationship.dto.response.PublicUserProfile;
import com.almonium.user.relationship.dto.response.RelatedUserProfile;
import com.almonium.user.relationship.model.entity.Relationship;
import com.almonium.user.relationship.model.enums.RelationshipStatus;
import com.almonium.user.relationship.model.projection.RelationshipToUserProjection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RelationshipRepository extends JpaRepository<Relationship, UUID> {
    @Query(
            """
            select new com.almonium.user.relationship.dto.response.PublicUserProfile(
                u.id,
                u.username,
                case when p.hidden = true then null else p.avatarUrl end
            )
            from User u
            left join u.profile p
            where u.username like %:username%
              and u.id != :currentUserId
              and not exists (
                  select 1
                  from Relationship r
                  where ((r.requester.id = :currentUserId and r.requestee.id = u.id)
                     or (r.requestee.id = :currentUserId and r.requester.id = u.id))
                     and r.status not in :retryableStatuses
              )
            """)
    List<PublicUserProfile> findNewFriendCandidates(
            UUID currentUserId, String username, List<RelationshipStatus> retryableStatuses);

    @Query(
            """
            select new com.almonium.user.relationship.dto.response.RelatedUserProfile(
                r.requestee.id,
                r.requestee.username,
                case when p.hidden = true then null else p.avatarUrl end,
                r.id,
                r.status
            )
            from User u
            join Relationship r on r.requester.id = u.id
            join Profile p on r.requestee.id = p.id
            where r.requester.id = :id and r.status = 'PENDING'
            """)
    List<RelatedUserProfile> getSentRequests(UUID id);

    @Query(
            """
            select new com.almonium.user.relationship.dto.response.RelatedUserProfile(
                r.requester.id,
                r.requester.username,
                case when p.hidden = true then null else p.avatarUrl end,
                r.id,
                r.status
            )
            from User u
            join Relationship r on r.requestee.id = u.id
            join Profile p on r.requester.id = p.id
            where r.requestee.id = :id and r.status = 'PENDING'
            """)
    List<RelatedUserProfile> getReceivedRequests(UUID id);

    @Query(
            """
        select new com.almonium.user.relationship.dto.response.RelatedUserProfile(
            u.id,
            u.username,
            case when p.hidden = true then null else p.avatarUrl end,
            r.id,
            r.status
        )
        from Relationship r
        join User u on
            (r.requester.id = u.id and r.status = 'SND_BLOCKED_FST' and r.requestee.id = :id)
            or
            (r.requestee.id = u.id and r.status = 'FST_BLOCKED_SND' and r.requester.id = :id)
            or
            (r.requester.id = u.id and r.status = 'MUTUAL_BLOCK' and r.requestee.id = :id)
            or
            (r.requestee.id = u.id and r.status = 'MUTUAL_BLOCK' and r.requester.id = :id)
        join Profile p on u.id = p.id
        """)
    List<RelatedUserProfile> getBlocked(UUID id);

    @Query(
            """
            select r from Relationship r
            where (r.requester.id = :id1 and r.requestee.id = :id2)
            or (r.requester.id = :id2 and r.requestee.id = :id1)
            """)
    Optional<Relationship> getRelationshipByUsersIds(UUID id1, UUID id2);

    /**
     * Retrieves a list of visible friendships for a given user.
     *
     * <p>This method performs a query on the Friendship table, returning a list of FriendshipToUserProjection objects,
     * which are projections that include the user's ID, the status of the relationship, and a boolean indicating
     * whether the user is the requester of the relationship.</p>
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
            select new com.almonium.user.relationship.model.projection.RelationshipToUserProjection(
                        case when r.requester.id = :id then r.requestee.id else r.requester.id end,
                        str(r.status),
                        case when r.requester.id = :id then true else false end)
                    from Relationship r
                    where (r.requester.id = :id or r.requestee.id = :id)
            and r.status = 'FRIENDS'
            """)
    List<RelationshipToUserProjection> getVisibleFriendships(UUID id); // TODO avoid writing FQN in query

    @Query(
            """
        select new com.almonium.user.relationship.dto.response.RelatedUserProfile(
            case
                when r.requester.id = :id then r.requestee.id
                else r.requester.id
            end,
            case
                when r.requester.id = :id then r.requestee.username
                else r.requester.username
            end,
            case
                when r.requester.id = :id then r.requestee.profile.avatarUrl
                else r.requester.profile.avatarUrl
            end,
            r.id,
            r.status
        )
        from Relationship r
        where (r.requester.id = :id or r.requestee.id = :id) and r.status = 'FRIENDS'
        """)
    List<RelatedUserProfile> getFriendships(UUID id);

    @Query(
            """
            select new com.almonium.user.relationship.model.projection.RelationshipToUserProjection(
                case when r.requester.id = :id then r.requestee.id else r.requester.id end,
                str(r.status),
                case when r.requester.id = :id then true else false end)
            from Relationship r
            join User u on (r.requester.id = u.id or r.requestee.id = u.id)
            where (r.requester.id = :id or r.requestee.id = :id)
              and r.status = 'FRIENDS'
              and u.username like %:username%
            """)
    List<RelationshipToUserProjection> searchFriendsByUsername(UUID id, String username);
}
