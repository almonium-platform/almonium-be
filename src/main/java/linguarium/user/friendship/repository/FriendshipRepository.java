package linguarium.user.friendship.repository;

import java.util.List;
import java.util.Optional;
import linguarium.user.friendship.model.Friendship;
import linguarium.user.friendship.model.FriendshipToUserProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FriendshipRepository extends JpaRepository<Friendship, Long> {
    @Query(
            """
            select f from Friendship f
            where (f.requester.id = :id1 and f.requestee.id = :id2)
            or (f.requester.id = :id2 and f.requestee.id = :id1)
            """)
    Optional<Friendship> getFriendshipByUsersIds(@Param("id1") long id1, @Param("id2") long id2);

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
                                    select new linguarium.user.friendship.model.FriendshipToUserProjection(
                        case when f.requester.id = :id then f.requestee.id else f.requester.id end,
                        str(f.status),
                        case when f.requester.id = :id then true else false end)
                    from Friendship f
                    where (f.requester.id = :id or f.requestee.id = :id)
            and not ((f.requester.id = :id and str(f.status) = 'SND_BLOCKED_FST')
                        or (f.requestee.id = :id and str(f.status) = 'FST_BLOCKED_SND'))
            """)
    List<FriendshipToUserProjection> getVisibleFriendships(@Param("id") long id);
}
