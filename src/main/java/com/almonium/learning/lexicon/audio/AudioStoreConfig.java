package com.almonium.learning.lexicon.audio;

import com.almonium.config.properties.AudioProperties;
import java.nio.file.Path;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** Picks the clip store from {@code app.audio.store}: a directory locally and in tests, the bucket when deployed. */
@Configuration
@RequiredArgsConstructor
public class AudioStoreConfig {
    private final AudioProperties properties;

    @Bean
    public AudioStore audioStore() {
        return switch (properties.getStore()) {
            case FILESYSTEM -> new FileSystemAudioStore(Path.of(properties.getLocalDir()));
            case S3 -> s3Store(properties.getS3());
        };
    }

    private static S3AudioStore s3Store(AudioProperties.S3 s3) {
        Map.of(
                        "AUDIO_S3_ENDPOINT", s3.getEndpoint(),
                        "AUDIO_S3_REGION", s3.getRegion(),
                        "AUDIO_S3_BUCKET", s3.getBucket(),
                        "AUDIO_S3_ACCESS_KEY_ID", s3.getAccessKeyId(),
                        "AUDIO_S3_SECRET_ACCESS_KEY", s3.getSecretAccessKey())
                .forEach((name, value) -> {
                    if (value == null || value.isBlank()) {
                        throw new IllegalStateException("app.audio.store is s3 but " + name + " is not set");
                    }
                });
        return S3AudioStore.connect(
                s3.getEndpoint(), s3.getRegion(), s3.getBucket(), s3.getAccessKeyId(), s3.getSecretAccessKey());
    }
}
