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

@Getter
@Setter
@Validated
@ConfigurationProperties(prefix = "google")
@FieldDefaults(level = PRIVATE)
public class GoogleProperties {

    @NotBlank
    String projectId;

    @NotBlank
    String parentLocation;

    @NotNull
    @Valid
    @NestedConfigurationProperty
    FirebaseProperties firebase = new FirebaseProperties();

    @NotNull
    @Valid
    @NestedConfigurationProperty
    Oauth2 oauth2 = new Oauth2();

    @Getter
    @Setter
    @FieldDefaults(level = PRIVATE)
    public static class FirebaseProperties {
        @NotBlank
        String serviceAccountKeyBase64;

        @NotNull
        @Valid
        @NestedConfigurationProperty
        StorageProperties storage = new StorageProperties();
    }

    @Getter
    @Setter
    @FieldDefaults(level = PRIVATE)
    public static class StorageProperties {
        @NotBlank
        String bucket;
    }

    @Getter
    @Setter
    @FieldDefaults(level = PRIVATE)
    public static class Oauth2 {
        @NotBlank
        String clientId;

        @NotBlank
        String clientSecret;
    }
}
