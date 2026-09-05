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

    /**
     * Where Paddle sends the buyer to complete a transaction, overriding the account's default payment link.
     *
     * <p>Blank by default, and blank means the account default - which is the only thing that works out of the box,
     * because Paddle applies its approved-domains check to a URL stated on a transaction but not to the default link
     * itself. Set it only to a domain listed under the dashboard's approved domains. Its reason to exist is local
     * development: a sandbox account has one default link shared with staging, so without an override a developer is
     * either sent to staging or has to repoint the link and break staging for everyone else.
     */
    String checkoutUrl = "";

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
