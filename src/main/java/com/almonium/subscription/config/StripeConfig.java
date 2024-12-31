package com.almonium.subscription.config;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.config.properties.StripeProperties;
import com.stripe.Stripe;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class StripeConfig {
    StripeProperties stripeProperties;

    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeProperties.getApi().getKey();
    }
}
