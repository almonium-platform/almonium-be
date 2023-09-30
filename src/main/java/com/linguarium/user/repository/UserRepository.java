package com.linguarium.user.repository;

import com.linguarium.friendship.model.FriendWrapper;
import com.linguarium.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u.id as id, u.username as username, u.email as email FROM User u WHERE u.email = :email")
    Optional<FriendWrapper> findFriendByEmail(String email);

    @Modifying
    @Query("update User u set u.username = ?1 where u.id = ?2")
    void changeUsername(String username, Long id);

    User findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
    Optional<FriendWrapper> findAllById(long id);
}
