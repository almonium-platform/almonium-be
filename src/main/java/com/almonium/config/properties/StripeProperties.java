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
@ConfigurationProperties(prefix = "stripe")
@FieldDefaults(level = PRIVATE)
public class StripeProperties {
    @NotBlank
    String returnUrl;

    @NotNull
    @Valid
    @NestedConfigurationProperty
    Webhook webhook = new Webhook();

    @NotNull
    @Valid
    @NestedConfigurationProperty
    Api api = new Api();

    @NotNull
    @Valid
    @NestedConfigurationProperty
    Checkout checkout = new Checkout();

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
    public static class Api {
        @NotBlank
        String key;
    }

    @Getter
    @Setter
    @FieldDefaults(level = PRIVATE)
    public static class Checkout {
        @NotBlank
        String successUrl;

        @NotBlank
        String cancelUrl;
    }
}
