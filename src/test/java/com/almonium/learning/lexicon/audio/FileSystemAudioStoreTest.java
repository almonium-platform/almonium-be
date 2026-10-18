package com.almonium.learning.lexicon.audio;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class FileSystemAudioStoreTest {
    private static final String KEY = "audio/en-GB/Charon/0123abcd.mp3";
    private static final byte[] CLIP = {(byte) 0xFF, (byte) 0xFB, (byte) 0x90, 0x64, 1, 2, 3};

    @TempDir
    Path root;

    @Test
    void knowsNothingOfAKeyNeverWritten() {
        FileSystemAudioStore store = new FileSystemAudioStore(root);

        assertThat(store.exists(KEY)).isFalse();
        assertThat(store.get(KEY)).isEmpty();
    }

    @Test
    void writesAClipUnderItsKeyAndReadsItBackAsMpegAudio() throws IOException {
        FileSystemAudioStore store = new FileSystemAudioStore(root);

        store.putIfAbsent(KEY, CLIP, "audio/mpeg");

        assertThat(store.exists(KEY)).isTrue();
        assertThat(store.get(KEY)).hasValueSatisfying(audio -> {
            assertThat(audio.bytes()).isEqualTo(CLIP);
            assertThat(audio.contentType()).isEqualTo("audio/mpeg");
        });
        assertThat(root.resolve(KEY)).isRegularFile();
        try (Stream<Path> files = Files.list(root.resolve("audio/en-GB/Charon"))) {
            assertThat(files).as("no staging file is left beside the clip").hasSize(1);
        }
    }

    @Test
    void neverOverwritesWhatIsAlreadyThere() {
        FileSystemAudioStore store = new FileSystemAudioStore(root);
        store.putIfAbsent(KEY, CLIP, "audio/mpeg");

        store.putIfAbsent(KEY, new byte[] {9, 9, 9}, "audio/mpeg");

        assertThat(store.get(KEY).orElseThrow().bytes()).isEqualTo(CLIP);
    }

    @Test
    void refusesAKeyThatWouldLeaveTheStore() {
        FileSystemAudioStore store = new FileSystemAudioStore(root);

        assertThatThrownBy(() -> store.putIfAbsent("../escaped.mp3", CLIP, "audio/mpeg"))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> store.exists("audio/../../escaped.mp3")).isInstanceOf(IllegalArgumentException.class);
        assertThat(root.getParent().resolve("escaped.mp3")).doesNotExist();
    }
}
