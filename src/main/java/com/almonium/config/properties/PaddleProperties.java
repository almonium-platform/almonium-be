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
@ConfigurationProperties(prefix = "paddle")
@FieldDefaults(level = PRIVATE)
public class PaddleProperties {
    @NotNull
    Environment environment;

    @NotBlank
    String clientToken;

    @NotNull
    @Valid
    @NestedConfigurationProperty
    Api api = new Api();

    @NotNull
    @Valid
    @NestedConfigurationProperty
    Webhook webhook = new Webhook();

    @NotNull
    @Valid
    @NestedConfigurationProperty
    Prices prices = new Prices();

    public String apiBaseUrl() {
        return environment == Environment.SANDBOX ? "https://sandbox-api.paddle.com" : "https://api.paddle.com";
    }

    public enum Environment {
        SANDBOX,
        LIVE
    }

    @Getter
    @Setter
    @FieldDefaults(level = PRIVATE)
    public static class Api {
        @NotBlank
        String key;
    }

    @Getter
    @Setter
    @FieldDefaults(level = PRIVATE)
    public static class Webhook {
        @NotBlank
        String secret;
    }

    @Getter
    @Setter
    @FieldDefaults(level = PRIVATE)
    public static class Prices {
        @NotBlank
        String premiumMonthly;

        @NotBlank
        String premiumAnnual;

        @NotBlank
        String founderMonthly;

        @NotBlank
        String founderAnnual;
    }
}
