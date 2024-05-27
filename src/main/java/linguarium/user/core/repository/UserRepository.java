package linguarium.user.core.repository;

import java.util.Optional;
import linguarium.user.core.model.User;
import linguarium.user.friendship.model.UserToFriendProjection;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("select u.id as id, u.username as username, u.email as email from User u where u.email = :email")
    Optional<UserToFriendProjection> findFriendByEmail(String email);

    @Modifying
    @Query("update User u set u.username = ?1 where u.id = ?2")
    void changeUsername(String username, long id);

    @EntityGraph(value = "graph.User.details", type = EntityGraph.EntityGraphType.LOAD)
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    Optional<UserToFriendProjection> findUserById(long id);
}
