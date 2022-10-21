package com.linguatool.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.linguatool.client.WordnikClient;
import com.linguatool.model.dto.SocialProvider;
import com.linguatool.model.entity.lang.*;
import com.linguatool.model.entity.user.Language;
import com.linguatool.model.entity.user.Role;
import com.linguatool.model.entity.user.User;
import com.linguatool.repository.*;
import com.linguatool.service.ExternalService;
import com.linguatool.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Profile("test")
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    ObjectMapper mapper;
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

    @Autowired
    private TranslatorRepository translatorRepository;

    @Autowired
    private LangPairTranslatorRepository langPairTranslatorRepository;

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private TranslationRepository translationRepository;

    @Autowired
    private ExampleRepository exampleRepository;

    @Autowired
    private TagRepository tagRepository;

    @SneakyThrows
    @Override
    @Transactional
    public void onApplicationEvent(final ContextRefreshedEvent event) {
        if (alreadySetup) {
            return;
        }

        Role userRole = createRoleIfNotFound(Role.ROLE_USER);
        Role adminRole = createRoleIfNotFound(Role.ROLE_ADMIN);
        persistLanguages();
        this.languageSetup();

        createUserIfNotFound("admin@mail.com", Set.of(userRole, adminRole));
        createUserIfNotFound("admin2@mail.com", Set.of(userRole, adminRole));
        createUserIfNotFound("admin3@mail.com", Set.of(userRole, adminRole));
        createUserIfNotFound("admin4@mail.com", Set.of(userRole, adminRole));
        createUserIfNotFound("admin5@mail.com", Set.of(userRole, adminRole));
        createUserIfNotFound("admin6@mail.com", Set.of(userRole, adminRole));
        createUserIfNotFound("admin7@mail.com", Set.of(userRole, adminRole));

//        createTestCards();

        System.out.println("F");

//        userService.createFriendshipRequest(2, 1);
//        userService.createFriendshipRequest(3, 1);
//        userService.createFriendshipRequest(4, 1);
//        userService.createFriendshipRequest(1, 5);
//        userService.createFriendshipRequest(1, 6);
//        userService.createFriendshipRequest(1, 7);
        alreadySetup = true;

    }

    @Transactional
    void languageSetup() {
        if (translatorRepository.getByName("YANDEX").isEmpty()) {
            translatorRepository.save(Translator.builder().name("YANDEX").build());
        }
        if (translatorRepository.getByName("GOOGLE").isEmpty()) {
            translatorRepository.save(Translator.builder().name("GOOGLE").build());
        }
        if (translatorRepository.getByName("DEEPL").isEmpty()) {
            translatorRepository.save(Translator.builder().name("DEEPL").build());
        }
        if (langPairTranslatorRepository
                .getByLangFromAndLangTo(
                        languageRepository.getEnglish(),
                        languageRepository.getUkrainian()
                ).isEmpty()) {

            langPairTranslatorRepository.save(LangPairTranslator.builder()
                    .langFromId(languageRepository.getEnglish().getId())
                    .langToId(languageRepository.getUkrainian().getId())
                    .translatorId(translatorRepository.getYandex().getId())
                    .build());
        }
        if (langPairTranslatorRepository
                .getByLangFromAndLangTo(
                        languageRepository.getEnglish(),
                        languageRepository.getRussian()
                ).isEmpty()) {
            langPairTranslatorRepository.save(LangPairTranslator.builder()
                    .langFromId(languageRepository.getEnglish().getId())
                    .langToId(languageRepository.getRussian().getId())
                    .translatorId(translatorRepository.getYandex().getId())
                    .build());
        }
    }

    @Transactional
    void createTestCards() {
        Card card = new Card();
        card.setOwner(userRepository.findByEmail("admin@mail.com"));
        card.setCreated(LocalDateTime.now());
        card.setUpdated(LocalDateTime.now());
        card.setActiveLearning(true);
        card.setNotes("Notes");
        card.setEntry("TEST ENTRY MAIN ONE");
        card.setFrequency(4);

        Example example = new Example();
        example.setExample("example1");
        example.setTranslation("translation1");
        example.setCard(card);

        Translation translation = new Translation();
        translation.setTranslation("translated no1");
        translation.setCard(card);

        card.setTranslations(List.of(translation));
        card.setExamples(List.of(example));
        card.setLanguage(languageRepository.getEnglish());

        cardRepository.save(card);

        translationRepository.save(translation);
        exampleRepository.save(example);

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
            user.setUsername("Admin" + email);
            user.setEmail(email);
            user.setPassword(passwordEncoder.encode("password"));
            user.setRoles(roles);
            user.setProvider(SocialProvider.LOCAL.getProviderType());
            user.setEnabled(true);
            LocalDateTime now = LocalDateTime.now();
            user.setCreated(now);
            user.setTargetLanguages(Set.of(languageRepository.getEnglish(), languageRepository.getGerman()));
            user.setFluentLanguages(Set.of(languageRepository.getUkrainian(), languageRepository.getRussian()));
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
