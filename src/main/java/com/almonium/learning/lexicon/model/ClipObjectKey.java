package com.almonium.learning.lexicon.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.regex.Pattern;

/**
 * Where one rendering of a pronunciation lives in the audio store: {@code audio/{variety}/{voice}/{hash}.{ext}},
 * the hash being SHA-256 of {@code engine|voice|model|pace|format|engineInput}. Two renderings with the same inputs
 * are the same clip, so the key is content-addressed and the store needs no overwrite. The variety and voice are
 * path segments, so they are held to a safe alphabet; only MP3 formats ({@code mp3_44100_128}) are accepted for now.
 */
public record ClipObjectKey(String variety, String voice, String hash, String extension) {
    private static final Pattern SEGMENT = Pattern.compile("[A-Za-z0-9._-]+");
    private static final String MP3_PREFIX = "mp3_";
    private static final int PACE_SCALE = 2;

    public static ClipObjectKey of(
            String variety,
            String engine,
            String voice,
            String model,
            BigDecimal pace,
            String format,
            String engineInput) {
        requireSegment("variety", variety);
        requireSegment("voice", voice);
        String rendering = String.join(
                "|",
                engine,
                voice,
                model,
                pace.setScale(PACE_SCALE, RoundingMode.HALF_UP).toPlainString(),
                format,
                engineInput);
        return new ClipObjectKey(variety, voice, sha256(rendering), extensionOf(format));
    }

    /** The content type every clip of the accepted formats is served with. */
    public static String extensionOf(String format) {
        if (format == null || !format.startsWith(MP3_PREFIX)) {
            throw new IllegalArgumentException("Unsupported audio format: " + format + " (only mp3_* for now)");
        }
        return "mp3";
    }

    /** The hash part of a stored key, which readers see as the clip's ETag. */
    public static String hashOf(String key) {
        int slash = key.lastIndexOf('/');
        int dot = key.lastIndexOf('.');
        return dot > slash ? key.substring(slash + 1, dot) : key.substring(slash + 1);
    }

    public String value() {
        return "audio/" + variety + "/" + voice + "/" + hash + "." + extension;
    }

    public String contentType() {
        return "audio/mpeg";
    }

    private static void requireSegment(String name, String value) {
        if (value == null || !SEGMENT.matcher(value).matches()) {
            throw new IllegalArgumentException("A clip's " + name + " must match " + SEGMENT + ": " + value);
        }
    }

    private static String sha256(String text) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return HexFormat.of().formatHex(digest.digest(text.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException exception) {
            throw new IllegalStateException(exception);
        }
    }
}
