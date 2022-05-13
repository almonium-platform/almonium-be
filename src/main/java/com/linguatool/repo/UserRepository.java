package com.linguatool.repo;

import com.linguatool.model.Friend;
import com.linguatool.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);

    @Query(value = "select * from account where email = ?1", nativeQuery = true)
    Optional<Friend> findFriendByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    Optional<Friend> findAllById(long id);
}
