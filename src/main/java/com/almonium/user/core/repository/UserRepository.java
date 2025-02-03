package com.almonium.user.core.repository;

import com.almonium.user.core.model.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {

    @EntityGraph(value = "graph.User.details", type = EntityGraph.EntityGraphType.LOAD)
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    @EntityGraph(attributePaths = {"principals"})
    Optional<User> findById(long id);

    Optional<User> findByStripeCustomerId(String stripeCustomerId);

    @Query("select u from User u join Learner l on u.id = l.user.id where u.id = :id")
    Optional<User> findUserWithLearners(long id);
}
