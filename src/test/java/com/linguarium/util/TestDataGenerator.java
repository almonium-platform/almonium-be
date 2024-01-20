package com.linguarium.util;

import com.google.protobuf.ByteString;
import com.linguarium.analyzer.dto.AnalysisDto;
import com.linguarium.analyzer.model.CEFR;
import com.linguarium.auth.model.LocalUser;
import com.linguarium.card.dto.*;
import com.linguarium.client.words.dto.WordsPronunciationDto;
import com.linguarium.client.words.dto.WordsReportDto;
import com.linguarium.client.words.dto.WordsResultDto;
import com.linguarium.client.words.dto.WordsSyllablesDto;
import com.linguarium.friendship.dto.FriendInfoDto;
import com.linguarium.friendship.dto.FriendshipActionDto;
import com.linguarium.friendship.model.FriendStatus;
import com.linguarium.friendship.model.Friendship;
import com.linguarium.friendship.model.FriendshipAction;
import com.linguarium.friendship.model.FriendshipStatus;
import com.linguarium.translator.dto.DefinitionDto;
import com.linguarium.translator.dto.MLTranslationCard;
import com.linguarium.translator.dto.TranslationCardDto;
import com.linguarium.user.model.Learner;
import com.linguarium.user.model.User;
import org.springframework.security.authentication.TestingAuthenticationToken;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public final class TestDataGenerator {
    private static final Random random = new Random();

    private TestDataGenerator() {
    }

    public static ByteString generateRandomAudioBytes() {
        byte[] byteArray = new byte[10];
        random.nextBytes(byteArray);
        return ByteString.copyFrom(byteArray);
    }

    public static TranslationCardDto createTranslationCardDto() {
        return TranslationCardDto.builder()
                .provider("GOOGLE")
                .definitions(new DefinitionDto[]{})
                .build();
    }

    public static TestingAuthenticationToken getAuthenticationToken(LocalUser principal) {
        return new TestingAuthenticationToken(
                principal,
                null,
                List.of());
    }

    public static TranslationCardDto createTestTranslationCardDto() {
        return TranslationCardDto.builder()
                .provider("ExampleProvider")
                .definitions(createTestDefinitionDtos())
                .build();
    }

    public static DefinitionDto[] createTestDefinitionDtos() {
        return new DefinitionDto[]{
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

    public static com.linguarium.translator.dto.TranslationDto[] createTestTranslationDtos() {
        return new com.linguarium.translator.dto.TranslationDto[]{
                com.linguarium.translator.dto.TranslationDto.builder()
                        .text("Translation 1")
                        .pos("Noun")
                        .frequency(5)
                        .build(),
                com.linguarium.translator.dto.TranslationDto.builder()
                        .text("Translation 2")
                        .pos("Verb")
                        .frequency(3)
                        .build()
        };
    }

    public static AnalysisDto createTestAnalysisDto() {
        return AnalysisDto.builder()
                .frequency(0.75)
                .cefr(CEFR.B1)
                .lemmas(new String[]{"lemma1", "lemma2"})
                .posTags(new String[]{"Noun", "Adjective"})
                .adjectives(new String[]{"adjective1", "adjective2"})
                .nouns(new String[]{"noun1", "noun2"})
                .foundCards(createCardDtos())
                .homophones(new String[]{"homophone1", "homophone2"})
                .family(new String[]{"word1", "word2"})
                .syllables(new String[]{"syllable1", "syllable2"})
                .isProper(true)
                .isForeignWord(false)
                .isPlural(true)
                .translationCards(createTestTranslationCardDto())
                .build();
    }

    public static CardDto[] createCardDtos() {
        CardDto card1 = CardDto.builder()
                .id(1L)
                .publicId("publicId1")
                .userId(1L)
                .entry("Hello")
                .language("English")
                .translations(new TranslationDto[]{new TranslationDto(1L, "Hola")})
                .created(LocalDateTime.now())
                .updated(LocalDateTime.now())
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
                .translations(new TranslationDto[]{new TranslationDto(2L, "Hello")})
                .created(LocalDateTime.now())
                .updated(LocalDateTime.now())
                .iteration(3)
                .priority(1)
                .activeLearning(true)
                .irregularPlural(true)
                .irregularSpelling(false)
                .falseFriend(false)
                .build();

        return new CardDto[]{card1, card2};
    }

    public static LocalUser createLocalUser() {
        User user = new User();
        user.setLearner(new Learner());
        return new LocalUser("user@example.com", "password", user);
    }

    public static WordsReportDto createEmptyWordsReportDto() {
        return WordsReportDto.builder()
                .word("word")
                .frequency(0.0)
                .results(new WordsResultDto[0])
                .syllables(WordsSyllablesDto.builder().build())
                .pronunciation(WordsPronunciationDto.builder().build())
                .build();
    }

    public static MLTranslationCard createMLTranslationCard() {
        return new MLTranslationCard("google", "test");
    }

    public static User buildTestUser() {
        User user = new User();
        user.setUsername("john");
        user.setEmail("john@email.com");
        user.setPassword("password");
        user.setProvider("local");
        user.setRegistered(LocalDateTime.now());
        return user;
    }

    public static CardCreationDto getCardCreationDto() {
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

    public static CardUpdateDto generateRandomCardUpdateDto() {
        CardUpdateDto cardUpdateDto = new CardUpdateDto();

        cardUpdateDto.setId(random.nextLong());
        cardUpdateDto.setEntry(generateRandomString());
        cardUpdateDto.setTranslations(generateRandomTranslationDtos());
        cardUpdateDto.setNotes(generateRandomString());
        cardUpdateDto.setTags(generateRandomTagDtos());
        cardUpdateDto.setExamples(generateRandomExampleDtos());
        cardUpdateDto.setCreated(LocalDateTime.now());
        cardUpdateDto.setLastRepeat(LocalDateTime.now());
        cardUpdateDto.setIteration(random.nextInt(10));
        cardUpdateDto.setUserId(random.nextLong());
        cardUpdateDto.setPriority(random.nextInt(5));
        cardUpdateDto.setTr_del(generateRandomIntArray());
        cardUpdateDto.setEx_del(generateRandomIntArray());
        cardUpdateDto.setUpdated(LocalDateTime.now());
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

    public static FriendInfoDto generateFriendInfoDto() {
        FriendInfoDto friendInfoDto = new FriendInfoDto();
        friendInfoDto.setStatus(FriendStatus.FRIENDS);
        friendInfoDto.setId(1L);
        friendInfoDto.setUsername("testuser");
        friendInfoDto.setEmail("test@example.com");
        return friendInfoDto;
    }

    public static FriendshipActionDto generateFriendshipActionDto() {
        FriendshipActionDto friendshipActionDto = new FriendshipActionDto();
        friendshipActionDto.setIdInitiator(1L);
        friendshipActionDto.setIdAcceptor(2L);
        friendshipActionDto.setAction(FriendshipAction.REQUEST);
        return friendshipActionDto;
    }

    public static List<FriendInfoDto> generateFriendInfoDtoList(int count) {
        List<FriendInfoDto> friendInfoDtoList = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            FriendInfoDto friendInfoDto = generateFriendInfoDto();
            friendInfoDto.setId((long) (i + 1));
            friendInfoDto.setUsername("user" + (i + 1));
            friendInfoDtoList.add(friendInfoDto);
        }
        return friendInfoDtoList;
    }

    public static Friendship generateFriendship(Long requesterId, Long requesteeId) {
        Friendship friendship = new Friendship();
        friendship.setRequesterId(requesterId);
        friendship.setRequesteeId(requesteeId);
        friendship.setCreated(LocalDateTime.now());
        friendship.setFriendshipStatus(FriendshipStatus.FRIENDS);
        return friendship;
    }
}
