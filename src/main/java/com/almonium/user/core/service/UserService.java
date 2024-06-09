package com.almonium.user.core.service;

import com.almonium.user.core.dto.UserInfo;
import com.almonium.user.core.model.entity.User;
import java.util.Optional;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    Optional<User> findByEmail(String email);

    User getById(Long id);

    UserInfo buildUserInfoFromUser(User user);

    boolean isUsernameAvailable(String username);

    void changeUsernameById(String username, Long id);

    void deleteAccount(User user);

    User getUserWithPrincipals(Long id);
}
