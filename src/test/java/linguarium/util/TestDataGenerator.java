package linguarium.util;

import com.google.protobuf.ByteString;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import linguarium.auth.oauth2.model.entity.Principal;
import linguarium.card.core.dto.CardCreationDto;
import linguarium.card.core.dto.CardDto;
import linguarium.card.core.dto.CardUpdateDto;
import linguarium.card.core.dto.ExampleDto;
import linguarium.card.core.dto.TagDto;
import linguarium.card.core.dto.TranslationDto;
import linguarium.card.core.model.entity.Card;
import linguarium.engine.analyzer.dto.AnalysisDto;
import linguarium.engine.analyzer.model.enums.CEFR;
import linguarium.engine.client.words.dto.WordsPronunciationDto;
import linguarium.engine.client.words.dto.WordsReportDto;
import linguarium.engine.client.words.dto.WordsResultDto;
import linguarium.engine.client.words.dto.WordsSyllablesDto;
import linguarium.engine.translator.dto.DefinitionDto;
import linguarium.engine.translator.dto.MLTranslationCard;
import linguarium.engine.translator.dto.TranslationCardDto;
import linguarium.engine.translator.model.enums.Language;
import linguarium.user.core.dto.UserInfo;
import linguarium.user.core.model.entity.Learner;
import linguarium.user.core.model.entity.Profile;
import linguarium.user.core.model.entity.User;
import linguarium.user.friendship.dto.FriendDto;
import linguarium.user.friendship.model.entity.Friendship;
import linguarium.user.friendship.model.enums.FriendStatus;
import linguarium.user.friendship.model.enums.FriendshipStatus;
import linguarium.user.friendship.model.projection.UserToFriendProjection;
import lombok.experimental.UtilityClass;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.AuthorizationGrantType;

@UtilityClass
public final class TestDataGenerator {
    private final Random random = new Random();

    public Random random() {
        return random;
    }

    public ByteString generateRandomAudioBytes() {
        byte[] byteArray = new byte[10];
        random.nextBytes(byteArray);
        return ByteString.copyFrom(byteArray);
    }

    public TranslationCardDto createTranslationCardDto() {
        return TranslationCardDto.builder()
                .provider("GOOGLE")
                .definitions(new DefinitionDto[] {})
                .build();
    }

    public TestingAuthenticationToken getAuthenticationToken(Principal principal) {
        return new TestingAuthenticationToken(principal, null, List.of());
    }

    public TranslationCardDto createTestTranslationCardDto() {
        return TranslationCardDto.builder()
                .provider("ExampleProvider")
                .definitions(createTestDefinitionDtos())
                .build();
    }

    public DefinitionDto[] createTestDefinitionDtos() {
        return new DefinitionDto[] {
            DefinitionDto.builder()
                    .text("Definition 1")
                    .pos("Noun")
                    .transcription("[ˌdɛfɪˈnɪʃən]")
                    .translations(createTestTranslationDtos())
                    .build(),
            DefinitionDto.builder()
                    .text("Definition 2")
                    .pos("Verb")
                    .transcription("[vɜːrb]")
                    .translations(createTestTranslationDtos())
                    .build()
        };
    }

    public linguarium.engine.translator.dto.TranslationDto[] createTestTranslationDtos() {
        return new linguarium.engine.translator.dto.TranslationDto[]{
                linguarium.engine.translator.dto.TranslationDto.builder()
                    .text("Translation 1")
                    .pos("Noun")
                    .frequency(5)
                    .build(),
                linguarium.engine.translator.dto.TranslationDto.builder()
                    .text("Translation 2")
                    .pos("Verb")
                    .frequency(3)
                    .build()
        };
    }

    public AnalysisDto createTestAnalysisDto() {
        return AnalysisDto.builder()
                .frequency(0.75)
                .cefr(CEFR.B1)
                .lemmas(new String[] {"lemma1", "lemma2"})
                .posTags(new String[] {"Noun", "Adjective"})
                .adjectives(new String[] {"adjective1", "adjective2"})
                .nouns(new String[] {"noun1", "noun2"})
                .foundCards(createCardDtos())
                .homophones(new String[] {"homophone1", "homophone2"})
                .family(new String[] {"word1", "word2"})
                .syllables(new String[] {"syllable1", "syllable2"})
                .isProper(true)
                .isForeignWord(false)
                .isPlural(true)
                .translationCards(createTestTranslationCardDto())
                .build();
    }

    public CardDto[] createCardDtos() {
        CardDto card1 = CardDto.builder()
                .id(1L)
                .publicId("publicId1")
                .userId(1L)
                .entry("Hello")
                .language("English")
                .translations(new TranslationDto[] {new TranslationDto(1L, "Hola")})
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .iteration(1)
                .priority(2)
                .activeLearning(true)
                .irregularPlural(false)
                .irregularSpelling(false)
                .falseFriend(true)
                .build();

        CardDto card2 = CardDto.builder()
                .id(2L)
                .publicId("publicId2")
                .userId(2L)
                .entry("Bonjour")
                .language("French")
                .translations(new TranslationDto[] {new TranslationDto(2L, "Hello")})
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .iteration(3)
                .priority(1)
                .activeLearning(true)
                .irregularPlural(true)
                .irregularSpelling(false)
                .falseFriend(false)
                .build();

        return new CardDto[] {card1, card2};
    }

    public WordsReportDto createEmptyWordsReportDto() {
        return WordsReportDto.builder()
                .word("word")
                .frequency(0.0)
                .results(new WordsResultDto[0])
                .syllables(WordsSyllablesDto.builder().build())
                .pronunciation(WordsPronunciationDto.builder().build())
                .build();
    }

    public MLTranslationCard createMLTranslationCard() {
        return new MLTranslationCard("google", "test");
    }

    public UserInfo buildTestUserInfo() {
        User user = buildTestUserWithId();
        Learner learner = Learner.builder()
                .id(user.getId())
                .targetLangs(Set.of(Language.EN, Language.ES))
                .fluentLangs(Set.of(Language.FR, Language.DE))
                .build();
        Profile profile = Profile.builder()
                .id(user.getId())
                .background("Background Image URL")
                .avatarUrl("Profile Image URL")
                .streak(3)
                .uiLang(Language.EN)
                .lastLogin(LocalDateTime.now())
                .build();

        return new UserInfo(
                user.getId().toString(),
                user.getUsername(),
                user.getEmail(),
                profile.getUiLang(),
                profile.getAvatarUrl(),
                profile.getBackground(),
                profile.getStreak(),
                learner.getTargetLangs(),
                learner.getFluentLangs(),
                List.of("tag1", "tag2", "tag3"));
    }

    public User buildTestUser() {
        User user = new User();
        user.setUsername("john");
        user.setEmail("john@email.com");
        user.setRegistered(LocalDateTime.now());
        return user;
    }

    public User buildTestUserWithId() {
        User user = new User();
        user.setId(1L);
        user.setUsername("john");
        user.setEmail("john@email.com");
        user.setRegistered(LocalDateTime.now());
        user.setProfile(Profile.builder().user(user).build());
        user.setLearner(Learner.builder().user(user).build());
        return user;
    }

    public Principal buildTestPrincipal() {
        User user = buildTestUserWithId();
        return Principal.builder().user(user).email(user.getEmail()).build();
    }

    public User buildTestUserWithId(long id) {
        User user = new User();
        user.setId(id);
        user.setUsername("john");
        user.setEmail("john@email.com");
        user.setRegistered(LocalDateTime.now());
        user.setProfile(Profile.builder().user(user).build());
        user.setLearner(Learner.builder().user(user).build());
        return user;
    }

    public User buildAnotherTestUser() {
        User user = new User();
        user.setUsername("jake");
        user.setEmail("jake@email.com");
        user.setRegistered(LocalDateTime.now());
        return user;
    }

    public CardCreationDto getCardCreationDto() {
        CardCreationDto cardCreationDto = new CardCreationDto();

        cardCreationDto.setEntry("Sample Entry");

        TranslationDto[] translations = new TranslationDto[2];
        translations[0] = new TranslationDto(1L, "Translation 1");
        translations[1] = new TranslationDto(2L, "Translation 2");
        cardCreationDto.setTranslations(translations);

        cardCreationDto.setNotes("Sample Notes");

        TagDto[] tags = new TagDto[2];
        tags[0] = new TagDto("Tag 1");
        tags[1] = new TagDto("Tag 2");
        cardCreationDto.setTags(tags);

        ExampleDto[] examples = new ExampleDto[2];
        examples[0] = new ExampleDto(1L, "Example 1", "Translation 1");
        examples[1] = new ExampleDto(2L, "Example 2", "Translation 2");
        cardCreationDto.setExamples(examples);

        cardCreationDto.setActiveLearning(true);
        cardCreationDto.setIrregularPlural(false);
        cardCreationDto.setFalseFriend(true);
        cardCreationDto.setIrregularSpelling(false);
        cardCreationDto.setLearnt(false);

        cardCreationDto.setLanguage("English");

        cardCreationDto.setCreated("2024-01-20T00:00:00");
        cardCreationDto.setUpdated("2024-01-20T12:30:00");

        cardCreationDto.setPriority(5);

        return cardCreationDto;
    }

    public CardUpdateDto generateRandomCardUpdateDto() {
        CardUpdateDto cardUpdateDto = new CardUpdateDto();

        cardUpdateDto.setId(random.nextLong());
        cardUpdateDto.setEntry(generateRandomString());
        cardUpdateDto.setTranslations(generateRandomTranslationDtos());
        cardUpdateDto.setNotes(generateRandomString());
        cardUpdateDto.setTags(generateRandomTagDtos());
        cardUpdateDto.setExamples(generateRandomExampleDtos());
        cardUpdateDto.setCreatedAt(LocalDateTime.now());
        cardUpdateDto.setLastRepeat(LocalDateTime.now());
        cardUpdateDto.setIteration(random.nextInt(10));
        cardUpdateDto.setUserId(random.nextLong());
        cardUpdateDto.setPriority(random.nextInt(5));
        cardUpdateDto.setDeletedTranslationsIds(generateRandomIntArray());
        cardUpdateDto.setDeletedExamplesIds(generateRandomIntArray());
        cardUpdateDto.setUpdatedAt(LocalDateTime.now());
        cardUpdateDto.setActiveLearning(random.nextBoolean());
        cardUpdateDto.setFalseFriend(random.nextBoolean());
        cardUpdateDto.setIrregularPlural(random.nextBoolean());
        cardUpdateDto.setIrregularSpelling(random.nextBoolean());
        cardUpdateDto.setLanguage(generateRandomString());

        return cardUpdateDto;
    }

    private static String generateRandomString() {
        int length = random.nextInt(10) + 1;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            char randomChar = (char) (random.nextInt(26) + 'a');
            sb.append(randomChar);
        }
        return sb.toString();
    }

    private static TranslationDto[] generateRandomTranslationDtos() {
        int numTranslations = random.nextInt(5);
        TranslationDto[] translations = new TranslationDto[numTranslations];
        for (int i = 0; i < numTranslations; i++) {
            translations[i] = new TranslationDto(random.nextLong(), generateRandomString());
        }
        return translations;
    }

    private static TagDto[] generateRandomTagDtos() {
        int numTags = random.nextInt(3);
        TagDto[] tags = new TagDto[numTags];
        for (int i = 0; i < numTags; i++) {
            tags[i] = new TagDto(generateRandomString());
        }
        return tags;
    }

    private static ExampleDto[] generateRandomExampleDtos() {
        int numExamples = random.nextInt(3);
        ExampleDto[] examples = new ExampleDto[numExamples];
        for (int i = 0; i < numExamples; i++) {
            examples[i] = new ExampleDto(random.nextLong(), generateRandomString(), generateRandomString());
        }
        return examples;
    }

    private static int[] generateRandomIntArray() {
        int length = random.nextInt(5);
        int[] array = new int[length];
        for (int i = 0; i < length; i++) {
            array[i] = random.nextInt(10);
        }
        return array;
    }

    public static UserToFriendProjection buildTestUserToFriendProjection(long id, String username, String email) {
        return new UserToFriendProjection() {
            @Override
            public long getId() {
                return id;
            }

            @Override
            public String getUsername() {
                return username;
            }

            @Override
            public String getEmail() {
                return email;
            }
        };
    }

    public FriendDto generateFriendInfoDto() {
        FriendDto friendDto = new FriendDto();
        friendDto.setStatus(FriendStatus.FRIENDS);
        friendDto.setId(1L);
        friendDto.setUsername("testuser");
        friendDto.setEmail("test@example.com");
        return friendDto;
    }

    public List<FriendDto> generateFriendInfoDtoList(int count) {
        List<FriendDto> friendDtoList = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            FriendDto friendDto = generateFriendInfoDto();
            friendDto.setId((long) (i + 1));
            friendDto.setUsername("userInfo" + (i + 1));
            friendDtoList.add(friendDto);
        }
        return friendDtoList;
    }

    public Friendship generateFriendship(Long requesterId, Long requesteeId) {
        Friendship friendship = new Friendship();
        friendship.setRequester(User.builder().id(requesterId).build());
        friendship.setRequestee(User.builder().id(requesteeId).build());
        friendship.setCreated(LocalDateTime.now());
        friendship.setStatus(FriendshipStatus.FRIENDS);
        return friendship;
    }

    public Card buildTestCard(UUID uuid, String entry, Learner owner) {
        Card card = new Card();
        card.setPublicId(uuid);
        card.setEntry(entry);
        card.setOwner(owner);
        card.setLanguage(Language.EN);
        card.setCreatedAt(LocalDateTime.now());
        return card;
    }

    public Card buildTestCard(Learner owner) {
        Card card = new Card();
        card.setPublicId(UUID.randomUUID());
        card.setEntry("dummyEntry");
        card.setOwner(owner);
        card.setLanguage(Language.EN);
        card.setCreatedAt(LocalDateTime.now());
        return card;
    }

    public Card buildTestCard() {
        Card card = new Card();
        card.setPublicId(UUID.randomUUID());
        card.setEntry("TEST_ENTRY");
        card.setLanguage(Language.EN);
        card.setCreatedAt(LocalDateTime.now());
        return card;
    }

    public static ClientRegistration buildClientRegistration() {
        return ClientRegistration.withRegistrationId("google")
                .clientId("test-client-id")
                .clientSecret("test-client-secret")
                .scope("openid", "profile", "email")
                .authorizationUri("https://accounts.google.com/o/oauth2/auth")
                .tokenUri("https://oauth2.googleapis.com/token")
                .userInfoUri("https://www.googleapis.com/oauth2/v3/userinfo")
                .userNameAttributeName("sub")
                .clientName("Google")
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .redirectUri("{baseUrl}/login/oauth2/code/{registrationId}")
                .build();
    }
}
