package com.almonium.config.properties;

import static lombok.AccessLevel.PRIVATE;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

@Getter
@Setter
@ConfigurationProperties(prefix = "stripe")
@FieldDefaults(level = PRIVATE)
public class StripeProperties {
    String returnUrl;

    @NestedConfigurationProperty
    Webhook webhook = new Webhook();

    @NestedConfigurationProperty
    Api api = new Api();

    @NestedConfigurationProperty
    Checkout checkout = new Checkout();

    @Getter
    @Setter
    @FieldDefaults(level = PRIVATE)
    public static class Webhook {
        String secret;
    }

    @Getter
    @Setter
    @FieldDefaults(level = PRIVATE)
    public static class Api {
        String key;
    }

    @Getter
    @Setter
    @FieldDefaults(level = PRIVATE)
    public static class Checkout {
        String successUrl;
        String cancelUrl;
    }
}
