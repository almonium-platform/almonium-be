package com.linguatool.configuration;

import com.linguatool.client.DatamuseClient;
import com.linguatool.client.WordnikClient;
import com.linguatool.client.WordsClient;
import com.linguatool.model.dto.SocialProvider;
import com.linguatool.model.entity.lang.LanguageEntity;
import com.linguatool.model.entity.user.Language;
import com.linguatool.model.entity.user.Role;
import com.linguatool.model.entity.user.User;
import com.linguatool.repository.FriendshipRepository;
import com.linguatool.repository.LanguageRepository;
import com.linguatool.repository.RoleRepository;
import com.linguatool.repository.UserRepository;
import com.linguatool.service.ExternalService;
import com.linguatool.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import net.sf.extjwnl.data.Word;
import org.apache.commons.codec.language.bm.Lang;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
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
    private ExternalService externalService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private LanguageRepository languageRepository;

    @Autowired
    private FriendshipRepository friendshipRepository;
    @Autowired
    private WordnikClient client;

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

//        LanguageEntity entity = new LanguageEntity();
//        entity.setCode(Language.ENGLISH);
//        languageRepository.save(entity);

//        client.getAudioFile("deranged");
//        datamuseClient.getHomophones("read");
//            ("lobotomy");
        persistLanguages();
        createUserIfNotFound("admin@mail.com", Set.of(userRole, adminRole));
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
    void persistLanguages() {
        Arrays.stream(Language.values()).sequential().forEach(t -> {
            Optional<LanguageEntity> languageEntityOptional = languageRepository.findByCode(t);
            if (languageEntityOptional.isEmpty()) {
                LanguageEntity entity = new LanguageEntity();
                entity.setCode(t);
                languageRepository.save(entity);
            }
        });
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
            user.setLearningLanguages(Set.of(languageRepository.findByCode(Language.ENGLISH).get(), languageRepository.findByCode(Language.GERMAN).get()));
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
