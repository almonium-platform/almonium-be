package com.almonium.util;

import com.almonium.analyzer.analyzer.dto.response.AnalysisDto;
import com.almonium.analyzer.analyzer.model.enums.CEFR;
import com.almonium.analyzer.client.words.dto.WordsPronunciationDto;
import com.almonium.analyzer.client.words.dto.WordsReportDto;
import com.almonium.analyzer.client.words.dto.WordsResultDto;
import com.almonium.analyzer.client.words.dto.WordsSyllablesDto;
import com.almonium.analyzer.translator.dto.DefinitionDto;
import com.almonium.analyzer.translator.dto.MLTranslationCard;
import com.almonium.analyzer.translator.dto.TranslationCardDto;
import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.auth.common.model.entity.Principal;
import com.almonium.auth.common.model.enums.AuthProviderType;
import com.almonium.auth.local.dto.request.LocalAuthRequest;
import com.almonium.auth.local.model.entity.LocalPrincipal;
import com.almonium.card.core.dto.ExampleDto;
import com.almonium.card.core.dto.TagDto;
import com.almonium.card.core.dto.TranslationDto;
import com.almonium.card.core.dto.request.CardCreationDto;
import com.almonium.card.core.dto.request.CardUpdateDto;
import com.almonium.card.core.dto.response.CardDto;
import com.almonium.card.core.model.entity.Card;
import com.almonium.infra.email.dto.EmailDto;
import com.almonium.subscription.model.entity.Plan;
import com.almonium.subscription.model.entity.PlanSubscription;
import com.almonium.subscription.model.entity.enums.PlanFeature;
import com.almonium.user.core.dto.response.SubscriptionInfoDto;
import com.almonium.user.core.dto.response.UserInfo;
import com.almonium.user.core.model.entity.Learner;
import com.almonium.user.core.model.entity.Profile;
import com.almonium.user.core.model.entity.User;
import com.almonium.user.core.model.enums.SetupStep;
import com.almonium.user.friendship.dto.response.PublicUserProfile;
import com.almonium.user.friendship.model.entity.Friendship;
import com.almonium.user.friendship.model.enums.FriendshipStatus;
import com.google.protobuf.ByteString;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.TestingAuthenticationToken;

// TODO a lot of unused code
@UtilityClass
public class TestDataGenerator {
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

    // todo different names to avoid fully qualified names
    public com.almonium.analyzer.translator.dto.TranslationDto[] createTestTranslationDtos() {
        return new com.almonium.analyzer.translator.dto.TranslationDto[] {
            com.almonium.analyzer.translator.dto.TranslationDto.builder()
                    .text("Translation 1")
                    .pos("Noun")
                    .frequency(5)
                    .build(),
            com.almonium.analyzer.translator.dto.TranslationDto.builder()
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
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
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
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
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
        Learner learner = Learner.builder().id(user.getId()).build();
        Profile profile = Profile.builder()
                .id(user.getId())
                .avatarUrl("Profile Image URL")
                .streak(3)
                .lastLogin(LocalDateTime.now())
                .build();

        PlanSubscription planSubscription = PlanSubscription.builder()
                .id(1L)
                .plan(Plan.builder().id(1L).build())
                .user(user)
                .build();

        SubscriptionInfoDto subscriptionInfo = SubscriptionInfoDto.builder()
                .name("Premium Plan")
                .autoRenewal(true)
                .limits(Map.of(PlanFeature.MAX_TARGET_LANGS, 1000))
                .type(Plan.Type.MONTHLY)
                .startDate(Instant.now())
                .endDate(Instant.now().plus(30, ChronoUnit.DAYS))
                .build();

        return UserInfo.builder()
                .id(user.getId().toString())
                .username(user.getUsername())
                .email(user.getEmail())
                .avatarUrl(profile.getAvatarUrl())
                .emailVerified(user.isEmailVerified())
                .setupStep(SetupStep.getInitial())
                .isPremium(false)
                .streak(profile.getStreak())
                .tags(List.of("tag1", "tag2"))
                .fluentLangs(Set.of(Language.FR, Language.DE))
                .subscription(subscriptionInfo)
                .build();
    }

    public User buildTestUser() {
        User user = new User();
        user.setUsername("john");
        user.setEmail("john@email.com");
        user.setRegistered(Instant.now());
        user.setSetupStep(SetupStep.getInitial());
        return user;
    }

    public User buildTestUserWithId() {
        User user = new User();
        user.setId(1L);
        user.setUsername("john");
        user.setEmail("john@email.com");
        user.setEmailVerified(true);
        user.setRegistered(Instant.now());
        user.setProfile(Profile.builder().user(user).build());
        user.setLearners(List.of(Learner.builder().user(user).build()));
        return user;
    }

    public Principal buildTestPrincipal() {
        User user = buildTestUserWithId();
        return LocalPrincipal.builder().user(user).email(user.getEmail()).build();
    }

    public Principal buildTestPrincipal(AuthProviderType providerType) {
        User user = buildTestUserWithId();
        return LocalPrincipal.builder()
                .user(user)
                .email(user.getEmail())
                .provider(providerType)
                .build();
    }

    public User buildTestUserWithId(UUID id) {
        User user = new User();
        user.setId(id);
        user.setUsername("john");
        user.setEmail("john@email.com");
        user.setRegistered(Instant.now());
        user.setProfile(Profile.builder().user(user).build());
        user.setLearners(List.of(Learner.builder().user(user).build()));
        return user;
    }

    public User buildAnotherTestUser() {
        User user = new User();
        user.setUsername("jake");
        user.setEmail("jake@email.com");
        user.setRegistered(Instant.now());
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

        cardCreationDto.setLanguage(Language.EN);

        cardCreationDto.setCreatedAt("2024-01-20T00:00:00");
        cardCreationDto.setUpdatedAt("2024-01-20T12:30:00");

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
        cardUpdateDto.setCreatedAt(Instant.now());
        cardUpdateDto.setLastRepeat(Instant.now());
        cardUpdateDto.setIteration(random.nextInt(10));
        cardUpdateDto.setUserId(random.nextLong());
        cardUpdateDto.setPriority(random.nextInt(5));
        cardUpdateDto.setDeletedTranslationsIds(generateRandomIntArray());
        cardUpdateDto.setDeletedExamplesIds(generateRandomIntArray());
        cardUpdateDto.setUpdatedAt(Instant.now());
        cardUpdateDto.setActiveLearning(random.nextBoolean());
        cardUpdateDto.setFalseFriend(random.nextBoolean());
        cardUpdateDto.setIrregularPlural(random.nextBoolean());
        cardUpdateDto.setIrregularSpelling(random.nextBoolean());
        cardUpdateDto.setLanguage(Language.EN);

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

    public PublicUserProfile generateFriendInfoDto() {
        PublicUserProfile PublicUserProfile = new PublicUserProfile();
        PublicUserProfile.setId(1L);
        PublicUserProfile.setUsername("testuser");
        return PublicUserProfile;
    }

    public List<PublicUserProfile> generateFriendInfoDtoList(int count) {
        List<PublicUserProfile> PublicUserProfileList = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            PublicUserProfile PublicUserProfile = generateFriendInfoDto();
            PublicUserProfile.setId(i + 1);
            PublicUserProfile.setUsername("userInfo" + (i + 1));
            PublicUserProfileList.add(PublicUserProfile);
        }
        return PublicUserProfileList;
    }

    public Friendship generateFriendship(Long requesterId, Long requesteeId) {
        Friendship friendship = new Friendship();
        friendship.setRequester(User.builder().id(requesterId).build());
        friendship.setRequestee(User.builder().id(requesteeId).build());
        friendship.setCreatedAt(Instant.now());
        friendship.setStatus(FriendshipStatus.FRIENDS);
        return friendship;
    }

    public Card buildTestCard(UUID uuid, String entry, Learner owner) {
        Card card = new Card();
        card.setPublicId(uuid);
        card.setEntry(entry);
        card.setOwner(owner);
        card.setLanguage(Language.EN);
        card.setCreatedAt(Instant.now());
        return card;
    }

    public Card buildTestCard(Learner owner) {
        Card card = new Card();
        card.setPublicId(UUID.randomUUID());
        card.setEntry("dummyEntry");
        card.setOwner(owner);
        card.setLanguage(Language.EN);
        card.setCreatedAt(Instant.now());
        return card;
    }

    public Card buildTestCard() {
        Card card = new Card();
        card.setPublicId(UUID.randomUUID());
        card.setEntry("TEST_ENTRY");
        card.setLanguage(Language.EN);
        card.setCreatedAt(Instant.now());
        return card;
    }

    public LocalAuthRequest createLocalAuthRequest() {
        return new LocalAuthRequest("dummy@example.com", "dummyPassword123");
    }

    public static String generateId() {
        return UUID.randomUUID().toString().replaceAll("-", StringUtils.EMPTY);
    }

    public static LocalPrincipal buildTestLocalPrincipal() {
        return (LocalPrincipal) TestDataGenerator.buildTestPrincipal(AuthProviderType.LOCAL);
    }

    public static EmailDto createEmailDto() {
        return new EmailDto("recipient@mail.com", "Subject", "Body");
    }

    public static Learner buildTestLearner() {
        return Learner.builder()
                .id(1L)
                .selfReportedLevel(CEFR.A1)
                .language(Language.EN)
                .build();
    }
}
