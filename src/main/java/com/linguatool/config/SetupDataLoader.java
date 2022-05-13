package com.linguatool.config;

import com.linguatool.dto.SocialProvider;
import com.linguatool.model.Friend;
import com.linguatool.model.user.Role;
import com.linguatool.model.user.User;
import com.linguatool.repo.FriendshipRepository;
import com.linguatool.repo.RoleRepository;
import com.linguatool.repo.UserRepository;
import com.linguatool.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    private boolean alreadySetup = false;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private FriendshipRepository friendshipRepository;

    @SneakyThrows
    @Override
    @Transactional
    public void onApplicationEvent(final ContextRefreshedEvent event) {
        if (alreadySetup) {
            return;
        }
//        userService.getUsersFriends(2L);
        // Create initial roles
        Role userRole = createRoleIfNotFound(Role.ROLE_USER);
        Role adminRole = createRoleIfNotFound(Role.ROLE_ADMIN);
//		createUserIfNotFound("admin@mail.com", Set.of(userRole, adminRole));
//		createUserIfNotFound("admin2@mail.com", Set.of(userRole, adminRole));
//		createUserIfNotFound("admin3@mail.com", Set.of(userRole, adminRole));
//		createUserIfNotFound("admin4@mail.com", Set.of(userRole, adminRole));
//		createUserIfNotFound("admin5@mail.com", Set.of(userRole, adminRole));
//		createUserIfNotFound("admin6@mail.com", Set.of(userRole, adminRole));
//		createUserIfNotFound("admin7@mail.com", Set.of(userRole, adminRole));
//        userService.createFriendshipRequest(2, 1);
//        userService.createFriendshipRequest(3, 1);
//        userService.createFriendshipRequest(4, 1);
//        userService.createFriendshipRequest(5, 1);
//        userService.createFriendshipRequest(6, 1);
//        userService.createFriendshipRequest(7, 1);
        alreadySetup = true;
    }

    @Transactional
    User createUserIfNotFound(final String email, Set<Role> roles) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            user = new User();
            user.setUsername("Admin");
            user.setEmail(email);
            user.setPassword(passwordEncoder.encode("password"));
            user.setRoles(roles);
            user.setProvider(SocialProvider.LOCAL.getProviderType());
            user.setEnabled(true);
            LocalDateTime now = LocalDateTime.now();
            user.setCreated(now);
            user.setModified(now);
            user = userRepository.save(user);
        }
        return user;
    }

    @Transactional
    Role createRoleIfNotFound(final String name) {
        Role role = roleRepository.findByName(name);
        if (role == null) {
            role = roleRepository.save(new Role(name));
        }
        return role;
    }
}
