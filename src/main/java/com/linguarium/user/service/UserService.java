package com.linguarium.user.service;

import com.linguarium.auth.dto.UserInfo;
import com.linguarium.user.model.User;
import java.util.Optional;

public interface UserService {
    UserInfo buildUserInfoFromUser(User user);

    void deleteAccount(User user);

    void changeUsernameById(String username, Long id);

    boolean isUsernameAvailable(String username);

    User findUserByEmail(String email);

    Optional<User> findUserById(Long id);
}
