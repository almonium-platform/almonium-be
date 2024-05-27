package linguarium.user.core.service;

import java.util.Optional;
import linguarium.user.core.dto.UserInfo;
import linguarium.user.core.model.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    Optional<User> findByEmail(String email);

    User getById(Long id);

    UserInfo buildUserInfoFromUser(User user);

    boolean isUsernameAvailable(String username);

    void changeUsernameById(String username, Long id);

    void deleteAccount(User user);
}
