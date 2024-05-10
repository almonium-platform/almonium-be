package com.linguarium.user.service;

import com.linguarium.auth.dto.UserInfo;
import com.linguarium.user.model.User;
import java.util.Optional;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    Optional<User> findByEmail(String email);

    User getById(Long id);

    UserInfo buildUserInfoFromUser(User user);

    boolean isUsernameAvailable(String username);

    void changeUsernameById(String username, Long id);

    void deleteAccount(User user);
}
