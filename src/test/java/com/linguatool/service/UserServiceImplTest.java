package com.linguatool.service;

import com.linguatool.exception.auth.UserAlreadyExistsAuthenticationException;
import com.linguatool.model.dto.*;
import com.linguatool.model.entity.lang.Language;
import com.linguatool.model.entity.lang.LanguageEntity;
import com.linguatool.model.entity.lang.Tag;
import com.linguatool.model.entity.user.Role;
import com.linguatool.model.entity.user.User;
import com.linguatool.repository.*;
import com.linguatool.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.Answer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static java.util.Map.entry;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceImplTest {
    @InjectMocks
    private UserServiceImpl userService;
    @Mock
    UserRepository userRepository;
    @Mock
    RoleRepository roleRepository;
    @Mock
    PasswordEncoder passwordEncoder;
    @Mock
    CardTagRepository cardTagRepository;
    @Mock
    TagRepository tagRepository;
    @Mock
    LanguageRepository languageRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userService = new UserServiceImpl(userRepository, roleRepository, passwordEncoder, cardTagRepository, tagRepository, languageRepository);
    }

    @Test
    @DisplayName("Should register new user")
    void givenValidUserDetailsFromGoogleProvider_whenProcessUserRegistration_thenReturnsLocalUser() {
        UserServiceImpl userServiceSpy = spy(userService);

        String sub = "101868015518714862283";
        String profilePicLink = "https://lh3.googleusercontent.com/a/AAcHTtdmMGFI1asVb1fp_pQ1ypkJqEHmI6Ug67ntQfLHYNqapw=s94-c";
        String password = "changeit";
        String registrationId = "google";
        Map<String, Object> attributes = Map.ofEntries(
                entry("at_hash", "RkJFPU-iZS_amRETFhGrdA"),
                entry("sub", sub),
                entry("email_verified", true),
                entry("iss", "https://accounts.google.com"),
                entry("given_name", "John"),
                entry("locale", "uk"),
                entry("nonce", "K3TiqNu1cgnErWX962crIutE8YiEjuQAd3PDzUV0E5M"),
                entry("picture", "https://lh3.googleusercontent.com/a/AAcHTtdmMGFI1asVb1fp_pQ1ypkJqEHmI6Ug67ntQfLHYNqapw=s94-c"),
                entry("aud", new String[]{"832714080763-hj64thg1sghaubbg9m6qd288mbv09li6.apps.googleusercontent.com"}),
                entry("azp", "832714080763-hj64thg1sghaubbg9m6qd288mbv09li6.apps.googleusercontent.com"),
                entry("name", "John Wick"),
                entry("exp", "2023-06-27T15:00:44Z"),
                entry("family_name", "Wick"),
                entry("iat", "2023-06-27T14:00:44Z"),
                entry("email", "johnwick@gmail.com")
        );

        OidcIdToken idToken = new OidcIdToken("random_token_value", null, null, attributes);
        OidcUserInfo userInfo = null;
        Role userRole = new Role(1L, Role.ROLE_USER);
        SignUpRequest signUpRequest = SignUpRequest.builder()
                .email("johnwick@gmail.com")
                .password(password)
                .providerUserId(sub)
                .socialProvider(SocialProvider.GOOGLE)
                .profilePicLink(profilePicLink)
                .build();

        User user = User.builder()
                .email("johnwick@gmail.com")
                .password(password)
                .providerUserId(sub)
                .roles(Set.of(userRole))
                .profilePicLink(profilePicLink)
                .enabled(true)
                .build();

        when(userRepository.existsByEmail("johnwick@gmail.com")).thenReturn(false);
        when(roleRepository.findByName(Role.ROLE_USER)).thenReturn(userRole);
        when(languageRepository.getEnglish()).thenReturn(new LanguageEntity(1L, Language.ENGLISH));
        doReturn(user).when(userServiceSpy).registerNewUser(signUpRequest);

        LocalUser result = userServiceSpy.processUserRegistration(registrationId, attributes, idToken, userInfo);

        verify(userServiceSpy).registerNewUser(eq(signUpRequest));

        assertThat(result).isNotNull();
        assertThat(result.getIdToken()).isEqualTo(idToken);
        assertThat(result.getAttributes()).isEqualTo(attributes);
        assertThat(result.getUserInfo()).isNull();
        assertThat(result.getUser()).isEqualTo(user);
    }

    @Test
    @DisplayName("Should throw an exception when trying to register user with existing userId")
    public void givenExistingUserId_whenRegisterNewUser_thenThrowUserAlreadyExistsAuthenticationException() {
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setUserID(1L);

        when(userRepository.existsById(signUpRequest.getUserID())).thenReturn(true);

        assertThrows(UserAlreadyExistsAuthenticationException.class,
                () -> userService.registerNewUser(signUpRequest));

        verify(userRepository).existsById(signUpRequest.getUserID());
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    @DisplayName("Should throw an exception when trying to register user with existing email")
    public void givenExistingUserEmail_whenRegisterNewUser_thenThrowUserAlreadyExistsAuthenticationException() {
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setEmail("john@example.com");

        when(userRepository.existsByEmail(signUpRequest.getEmail())).thenReturn(true);

        assertThrows(UserAlreadyExistsAuthenticationException.class,
                () -> userService.registerNewUser(signUpRequest));

        verify(userRepository).existsByEmail(signUpRequest.getEmail());
        verify(userRepository, never()).existsById(anyLong());
        verify(userRepository, never()).existsByUsername(anyString());
        verify(userRepository, never()).save(any(User.class));
        verify(userRepository, never()).flush();
    }

    @Test
    @DisplayName("Should throw an exception when trying to register user with existing username")
    public void givenExistingUsername_whenRegisterNewUser_thenThrowUserAlreadyExistsAuthenticationException() {
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setUsername("john");

        when(userRepository.existsByUsername(signUpRequest.getUsername())).thenReturn(true);

        assertThrows(UserAlreadyExistsAuthenticationException.class, () -> {
            userService.registerNewUser(signUpRequest);
        });

        verify(userRepository).existsByUsername(signUpRequest.getUsername());
        verify(userRepository, never()).existsById(anyLong());
        verify(userRepository, never()).existsByEmail(anyString());
        verify(userRepository, never()).save(any(User.class));
        verify(userRepository, never()).flush();
    }

    @Test
    @DisplayName("Should change username")
    public void givenUsername_whenChangeUsername_thenUsernameChanged() {
        String username = "newUsername";
        Long id = 1L;

        when(userRepository.existsByUsername(username)).thenReturn(false);

        userService.changeUsername(username, id);

        verify(userRepository).existsByUsername(username);
        verify(userRepository).changeUsername(username, id);
    }

    @Test
    @DisplayName("Shouldn't change username for existing username")
    public void givenExistingUsername_whenChangeUsername_thenUsernameNotChanged() {
        String username = "existingUsername";
        Long id = 1L;

        when(userRepository.existsByUsername(username)).thenReturn(true);

        userService.changeUsername(username, id);

        verify(userRepository).existsByUsername(username);
        verify(userRepository, never()).changeUsername(anyString(), anyLong());
    }

    @Test
    @DisplayName("Should return user optional for existing user")
    public void givenExistingUser_whenFindUserById_thenReturnUserOptional() {
        Long userId = 1L;
        User user = new User();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        Optional<User> result = userService.findUserById(userId);

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(user);
        verify(userRepository).findById(userId);
    }

    @Test
    @DisplayName("Should return empty optional for non existing user")
    public void givenNonExistingUser_whenFindUserById_thenReturnEmptyOptional() {
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        Optional<User> result = userService.findUserById(userId);

        assertThat(result).isEmpty();
        verify(userRepository).findById(userId);
    }

    @Test
    @DisplayName("Should delete user account")
    public void givenUser_whenDeleteAccount_thenRepositoryDeleteIsCalled() {
        User user = new User();

        userService.deleteAccount(user);

        verify(userRepository).delete(user);
    }

    @Test
    @DisplayName("Should return user if email exists")
    public void givenExistentEmail_whenFindByEmail_thenReturnUser() {
        String email = "john@example.com";
        User expectedUser = new User();
        expectedUser.setEmail(email);

        when(userRepository.findByEmail(email)).thenReturn(expectedUser);

        User actualUser = userService.findUserByEmail(email);

        assertThat(expectedUser).isEqualTo(actualUser);
    }

    @Test
    @DisplayName("Should return null if email doesn't exist")
    public void givenNonExistentEmail_whenFindByEmail_thenReturnNull() {
        String email = "nonexistent@example.com";

        when(userRepository.findByEmail(email)).thenReturn(null);

        User actualUser = userService.findUserByEmail(email);

        assertThat(actualUser).isNull();
    }

    @Test
    @DisplayName("Should build UserInfo from LocalUser")
    void givenLocalUser_whenBuildUserInfo_thenReturnUserInfo() {
        com.linguatool.model.entity.user.User user = new com.linguatool.model.entity.user.User();
        user.setId(1L);
        user.setUsername("john");
        user.setPassword("password");
        user.setEmail("john@example.com");
        user.setUiLanguage(Language.ENGLISH);
        user.setProfilePicLink("profile.jpg");
        user.setBackground("background.jpg");
        user.setStreak(5);
        Set<GrantedAuthority> authorities = Set.of(new SimpleGrantedAuthority("ROLE_USER"));
        user.setTargetLanguages(Set.of(
                new LanguageEntity(2L, Language.GERMAN),
                new LanguageEntity(3L, Language.FRENCH)
        ));
        user.setFluentLanguages(Set.of(
                new LanguageEntity(4L, Language.SPANISH),
                new LanguageEntity(5L, Language.RUSSIAN)
        ));

        OidcIdToken idToken = mock(OidcIdToken.class);
        OidcUserInfo oidcUserInfo = mock(OidcUserInfo.class);

        LocalUser localUser = new LocalUser(user.getEmail(), user.getPassword(), user.isEnabled(), true, true, true,
                authorities, user, idToken, oidcUserInfo);

        Set<Long> tagIds = Set.of(1L, 2L, 3L);
        when(cardTagRepository.getUsersTags(user)).thenReturn(tagIds);
        when(tagRepository.getById(anyLong())).thenAnswer(invocation -> {
            Long tagId = invocation.getArgument(0);
            return new Tag(tagId, "Tag " + tagId);
        });

        UserInfo userInfo = userService.buildUserInfo(localUser);

        assertThat(userInfo).isNotNull()
                .extracting(UserInfo::getId, UserInfo::getUsername, UserInfo::getEmail, UserInfo::getUiLang,
                        UserInfo::getProfilePicLink, UserInfo::getBackground, UserInfo::getStreak)
                .containsExactly("1", "john", "john@example.com", Language.ENGLISH.getCode(),
                        "profile.jpg", "background.jpg", 5);
        assertThat(userInfo.getRoles()).containsExactly("ROLE_USER");
        assertThat(userInfo.getTags()).containsExactlyInAnyOrder("tag_1", "tag_2", "tag_3");
        assertThat(userInfo.getTargetLangs()).containsExactlyInAnyOrder(Language.GERMAN.getCode(), Language.FRENCH.getCode());
        assertThat(userInfo.getFluentLangs()).containsExactlyInAnyOrder(Language.SPANISH.getCode(), Language.RUSSIAN.getCode());

        verify(cardTagRepository).getUsersTags(user);
        tagIds.forEach(tagId -> verify(tagRepository).getById(eq(tagId)));
    }

    @Test
    @DisplayName("Should set new fluent languages for user with existing target languages")
    void givenUserWithExistingTargetLanguages_whenSetTargetLangs_thenNewLanguagesSet() {
        LangCodeDto dto = new LangCodeDto(new String[]{"DE", "FR", "ES"});
        User user = new User();
        Set<LanguageEntity> existingLanguages = Set.of(
                new LanguageEntity(1L, Language.ENGLISH),
                new LanguageEntity(2L, Language.GERMAN),
                new LanguageEntity(3L, Language.FRENCH)
        );
        user.setTargetLanguages(existingLanguages);

        Set<LanguageEntity> mockedLanguages = Set.of(
                new LanguageEntity(2L, Language.GERMAN),
                new LanguageEntity(3L, Language.FRENCH),
                new LanguageEntity(4L, Language.SPANISH)
        );

        when(languageRepository.findByCode(any(Language.class)))
                .thenAnswer(invocation -> {
                    Language code = invocation.getArgument(0);
                    return mockedLanguages.stream()
                            .filter(lang -> lang.getCode() == code)
                            .findFirst();
                });

        userService.setTargetLangs(dto, user);

        Set<LanguageEntity> updatedLanguages = user.getTargetLanguages();
        assertThat(updatedLanguages)
                .as("The new target languages should be set correctly")
                .hasSize(dto.getCodes().length)
                .extracting(LanguageEntity::getCode)
                .containsExactlyInAnyOrder(Language.GERMAN, Language.FRENCH, Language.SPANISH);

        verify(languageRepository, times(3)).findByCode(any(Language.class));
        verify(userRepository).save(user);
    }

    @Test
    @DisplayName("Should set target languages for user based on language code DTO")
    void givenLangCodeDto_whenSetTargetLangs_thenUserTargetLanguagesAreSet() {
        LangCodeDto dto = new LangCodeDto();
        dto.setCodes(new String[]{"EN", "DE"});

        User user = new User();
        Set<LanguageEntity> languages = new HashSet<>();
        when(languageRepository.findByCode(any(Language.class))).thenAnswer((Answer<Optional<LanguageEntity>>) invocation -> {
            Language code = invocation.getArgument(0);
            LanguageEntity languageEntity = new LanguageEntity();
            languageEntity.setCode(code);
            languages.add(languageEntity);
            return Optional.of(languageEntity);
        });

        userService.setTargetLangs(dto, user);

        verify(languageRepository, times(2)).findByCode(any(Language.class));
        assertThat(user.getTargetLanguages()).hasSize(2).containsExactlyInAnyOrderElementsOf(languages);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    @DisplayName("Should set new fluent languages for user with existing fluent languages")
    void givenUserWithExistingFluentLanguages_whenSetFluentLangs_thenNewLanguagesSet() {
        LangCodeDto dto = new LangCodeDto(new String[]{"DE", "FR", "ES"});
        User user = new User();
        Set<LanguageEntity> existingLanguages = Set.of(
                new LanguageEntity(1L, Language.ENGLISH),
                new LanguageEntity(2L, Language.GERMAN),
                new LanguageEntity(3L, Language.FRENCH)
        );
        user.setFluentLanguages(existingLanguages);

        Set<LanguageEntity> mockedLanguages = Set.of(
                new LanguageEntity(2L, Language.GERMAN),
                new LanguageEntity(3L, Language.FRENCH),
                new LanguageEntity(4L, Language.SPANISH)
        );

        when(languageRepository.findByCode(any(Language.class)))
                .thenAnswer(invocation -> {
                    Language code = invocation.getArgument(0);
                    return mockedLanguages.stream()
                            .filter(lang -> lang.getCode() == code)
                            .findFirst();
                });

        userService.setFluentLangs(dto, user);

        Set<LanguageEntity> updatedLanguages = user.getFluentLanguages();
        assertThat(updatedLanguages)
                .as("The new fluent languages should be set correctly")
                .hasSize(dto.getCodes().length)
                .extracting(LanguageEntity::getCode)
                .containsExactlyInAnyOrder(Language.GERMAN, Language.FRENCH, Language.SPANISH);

        verify(languageRepository, times(3)).findByCode(any(Language.class));
        verify(userRepository).save(user);
    }

    @Test
    @DisplayName("Should set fluent languages for user based on language code DTO")
    void givenLangCodeDto_whenSetFluentLangs_thenUserFluentLanguagesAreSet() {
        LangCodeDto dto = new LangCodeDto();
        dto.setCodes(new String[]{"EN", "DE"});

        User user = new User();
        Set<LanguageEntity> languages = new HashSet<>();
        when(languageRepository.findByCode(any(Language.class))).thenAnswer((Answer<Optional<LanguageEntity>>) invocation -> {
            Language code = invocation.getArgument(0);
            LanguageEntity languageEntity = new LanguageEntity();
            languageEntity.setCode(code);
            languages.add(languageEntity);
            return Optional.of(languageEntity);
        });

        userService.setFluentLangs(dto, user);

        verify(languageRepository, times(2)).findByCode(any(Language.class));
        assertThat(user.getFluentLanguages()).hasSize(2).containsExactlyInAnyOrderElementsOf(languages);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    @DisplayName("Should increase streak if last login is on the previous day")
    void givenLastLoginIsPreviousDay_whenUpdateLoginStreak_thenStreakIsIncreased() {
        User user = new User();
        LocalDateTime lastLogin = LocalDateTime.now().minusDays(1);
        user.setLastLogin(lastLogin);
        user.setStreak(5);

        userService.updateLoginStreak(user);

        assertThat(user.getStreak()).isEqualTo(6);
        assertThat(user.getLastLogin().toLocalDate()).isEqualTo(LocalDate.now());
    }

    @Test
    @DisplayName("Should reset streak if last login is not on the previous day")
    void givenLastLoginNotPreviousDay_whenUpdateLoginStreak_thenStreakIsReset() {
        User user = new User();
        LocalDateTime lastLogin = LocalDateTime.now().minusDays(2);
        user.setLastLogin(lastLogin);
        user.setStreak(5);

        userService.updateLoginStreak(user);

        assertThat(user.getStreak()).isEqualTo(1);
        assertThat(user.getLastLogin().toLocalDate()).isEqualTo(LocalDate.now());
    }
}
