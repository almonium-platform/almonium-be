package com.almonium.config.properties;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

@Getter
@Setter
@ConfigurationProperties(prefix = "google")
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class GoogleProperties {
    String projectId;
    String parentLocation;

    @NestedConfigurationProperty
    FirebaseProperties firebase = new FirebaseProperties();

    @NestedConfigurationProperty
    Oauth2 oauth2 = new Oauth2();

    @Getter
    @Setter
    @FieldDefaults(level = lombok.AccessLevel.PRIVATE)
    public static class FirebaseProperties {
        String serviceAccountKeyBase64;

        @NestedConfigurationProperty
        StorageProperties storage = new StorageProperties();
    }

    @Getter
    @Setter
    @FieldDefaults(level = lombok.AccessLevel.PRIVATE)
    public static class StorageProperties {
        String bucket;
    }

    @Getter
    @Setter
    @FieldDefaults(level = lombok.AccessLevel.PRIVATE)
    public static class Oauth2 {
        String clientId;
        String clientSecret;
    }
}
