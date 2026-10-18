package com.almonium.learning.lexicon.audio;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.Optional;

/**
 * Clips as files under one directory, for local work and tests. A clip is written to a temporary file beside its
 * final name and moved into place atomically, so a half-written file is never served and two writers of the same
 * key leave exactly one clip. The content type is read back from the extension, which the key scheme fixes.
 */
public class FileSystemAudioStore implements AudioStore {
    private final Path root;

    public FileSystemAudioStore(Path root) {
        this.root = root.toAbsolutePath().normalize();
    }

    @Override
    public boolean exists(String key) {
        return Files.isRegularFile(resolve(key));
    }

    @Override
    public void putIfAbsent(String key, byte[] bytes, String contentType) {
        Path target = resolve(key);
        if (Files.exists(target)) {
            return;
        }
        // resolve() never hands back the root itself, so the clip always has a directory above it.
        Path directory = Objects.requireNonNull(target.getParent());
        try {
            Files.createDirectories(directory);
            Path staging = Files.createTempFile(directory, ".", ".part");
            try {
                Files.write(staging, bytes);
                Files.move(staging, target, StandardCopyOption.ATOMIC_MOVE);
            } catch (FileAlreadyExistsException raced) {
                // Another writer got there first with the same bytes; theirs stand.
            } finally {
                Files.deleteIfExists(staging);
            }
        } catch (IOException exception) {
            throw new UncheckedIOException("Cannot store audio clip " + key, exception);
        }
    }

    @Override
    public Optional<StoredAudio> get(String key) {
        Path file = resolve(key);
        if (!Files.isRegularFile(file)) {
            return Optional.empty();
        }
        try {
            return Optional.of(new StoredAudio(Files.readAllBytes(file), contentTypeOf(key)));
        } catch (IOException exception) {
            throw new UncheckedIOException("Cannot read audio clip " + key, exception);
        }
    }

    private Path resolve(String key) {
        Path path = root.resolve(key).normalize();
        if (!path.startsWith(root) || path.equals(root)) {
            throw new IllegalArgumentException("Audio key escapes the store: " + key);
        }
        return path;
    }

    private static String contentTypeOf(String key) {
        return key.endsWith(".mp3") ? "audio/mpeg" : "application/octet-stream";
    }
}
