package com.almonium.learning.lexicon.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.HexFormat;
import org.junit.jupiter.api.Test;

class ClipObjectKeyTest {
    private static final String FORMAT = "mp3_44100_128";

    @Test
    void namesAClipByVarietyVoiceAndTheHashOfItsRendering() throws Exception {
        ClipObjectKey key =
                ClipObjectKey.of("en-GB", "google", "Charon", "chirp3-hd", new BigDecimal("1"), FORMAT, "bloke");

        String expectedHash = HexFormat.of()
                .formatHex(MessageDigest.getInstance("SHA-256")
                        .digest("google|Charon|chirp3-hd|1.00|mp3_44100_128|bloke".getBytes(StandardCharsets.UTF_8)));
        assertThat(key.hash()).hasSize(64).isEqualTo(expectedHash);
        assertThat(key.value()).isEqualTo("audio/en-GB/Charon/" + expectedHash + ".mp3");
        assertThat(key.contentType()).isEqualTo("audio/mpeg");
        assertThat(ClipObjectKey.hashOf(key.value())).isEqualTo(expectedHash);
    }

    @Test
    void rendersThePaceWithTwoDecimalsSoEqualPacesWrittenDifferentlyAreOneClip() {
        ClipObjectKey one = ClipObjectKey.of("en-GB", "google", "Charon", "m", new BigDecimal("1"), FORMAT, "bloke");
        ClipObjectKey onePointZero =
                ClipObjectKey.of("en-GB", "google", "Charon", "m", new BigDecimal("1.0"), FORMAT, "bloke");
        ClipObjectKey slower =
                ClipObjectKey.of("en-GB", "google", "Charon", "m", new BigDecimal("0.9"), FORMAT, "bloke");

        assertThat(onePointZero).isEqualTo(one);
        assertThat(slower).isNotEqualTo(one);
    }

    @Test
    void everyPartOfTheRenderingTellsClipsApart() {
        ClipObjectKey base = ClipObjectKey.of("en-GB", "google", "Charon", "m", BigDecimal.ONE, FORMAT, "bloke");

        assertThat(ClipObjectKey.of("en-GB", "eleven", "Charon", "m", BigDecimal.ONE, FORMAT, "bloke"))
                .isNotEqualTo(base);
        assertThat(ClipObjectKey.of("en-GB", "google", "Charon", "m2", BigDecimal.ONE, FORMAT, "bloke"))
                .isNotEqualTo(base);
        assertThat(ClipObjectKey.of("en-GB", "google", "Charon", "m", BigDecimal.ONE, "mp3_22050_32", "bloke"))
                .isNotEqualTo(base);
        assertThat(ClipObjectKey.of("en-GB", "google", "Charon", "m", BigDecimal.ONE, FORMAT, "Bloke."))
                .isNotEqualTo(base);
        assertThat(ClipObjectKey.of("en-US", "google", "Charon", "m", BigDecimal.ONE, FORMAT, "bloke")
                        .value())
                .startsWith("audio/en-US/Charon/")
                .endsWith(base.hash() + ".mp3");
    }

    @Test
    void refusesFormatsOtherThanMp3AndPathSegmentsThatAreNotSafe() {
        assertThatThrownBy(
                        () -> ClipObjectKey.of("en-GB", "google", "Charon", "m", BigDecimal.ONE, "wav_44100", "bloke"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("wav_44100");
        assertThatThrownBy(() -> ClipObjectKey.of("en-GB", "google", "Charon", "m", BigDecimal.ONE, null, "bloke"))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> ClipObjectKey.of("en/GB", "google", "Charon", "m", BigDecimal.ONE, FORMAT, "bloke"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("variety");
        assertThatThrownBy(() -> ClipObjectKey.of("en-GB", "google", "../Charon", "m", BigDecimal.ONE, FORMAT, "bloke"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("voice");
    }
}
