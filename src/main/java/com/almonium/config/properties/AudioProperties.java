package com.almonium.config.properties;

import static lombok.AccessLevel.PRIVATE;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.validation.annotation.Validated;

/**
 * Where pronunciation clips are kept: a directory for local work and tests, an S3-compatible bucket (Cloudflare R2)
 * when deployed. The S3 values are only read when {@code store} is {@code s3}, which is why they carry no constraints
 * of their own; the store configuration checks them when it builds the client.
 */
@Getter
@Setter
@Validated
@ConfigurationProperties(prefix = "app.audio")
@FieldDefaults(level = PRIVATE)
public class AudioProperties {
    @NotNull
    Store store = Store.FILESYSTEM;

    @NotBlank
    String localDir = "./out/audio";

    @NotNull
    @Valid
    @NestedConfigurationProperty
    S3 s3 = new S3();

    public enum Store {
        FILESYSTEM,
        S3
    }

    @Getter
    @Setter
    @FieldDefaults(level = PRIVATE)
    public static class S3 {
        String endpoint;
        String region;
        String bucket;
        String accessKeyId;
        String secretAccessKey;
    }
}
