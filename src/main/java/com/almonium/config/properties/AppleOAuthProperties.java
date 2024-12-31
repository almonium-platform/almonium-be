package com.almonium.config.properties;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

@Getter
@Setter
@ConfigurationProperties(prefix = "spring.security.oauth2.client.registration.apple")
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class AppleOAuthProperties {
    String clientId;
    String clientSecret;
    String redirectUri;
    String authorizationGrantType;

    @NestedConfigurationProperty
    Provider provider = new Provider();

    @Getter
    @Setter
    @FieldDefaults(level = lombok.AccessLevel.PRIVATE)
    public static class Provider {
        String jwkSetUri;
    }
}
