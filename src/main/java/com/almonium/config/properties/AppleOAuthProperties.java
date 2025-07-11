package com.almonium.config.properties;

import static lombok.AccessLevel.PRIVATE;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Validated
@ConfigurationProperties(prefix = "spring.security.oauth2.client.registration.apple")
@FieldDefaults(level = PRIVATE)
public class AppleOAuthProperties {
    @NotBlank
    String clientId;

    @NotBlank
    String clientSecret;

    @NotBlank
    String redirectUri;

    @NotBlank
    String authorizationGrantType;
}
