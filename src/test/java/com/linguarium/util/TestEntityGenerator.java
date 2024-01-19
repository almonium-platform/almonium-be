package com.linguarium.util;

import com.google.protobuf.ByteString;
import com.linguarium.analyzer.dto.AnalysisDto;
import com.linguarium.analyzer.model.CEFR;
import com.linguarium.auth.model.LocalUser;
import com.linguarium.card.dto.CardDto;
import com.linguarium.card.dto.TranslationDto;
import com.linguarium.client.words.dto.WordsPronunciationDto;
import com.linguarium.client.words.dto.WordsReportDto;
import com.linguarium.client.words.dto.WordsResultDto;
import com.linguarium.client.words.dto.WordsSyllablesDto;
import com.linguarium.translator.dto.DefinitionDto;
import com.linguarium.translator.dto.MLTranslationCard;
import com.linguarium.translator.dto.TranslationCardDto;
import com.linguarium.user.model.Learner;
import com.linguarium.user.model.User;
import org.springframework.security.authentication.TestingAuthenticationToken;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

public final class TestEntityGenerator {
    private static final Random random = new Random();

    private TestEntityGenerator() {
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
}
