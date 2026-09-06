package com.almonium.config.properties;

import static lombok.AccessLevel.PRIVATE;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

/**
 * The organisation-level side of OpenAI: the admin key that can read the bill, and the price list the spend page
 * uses to turn our own token ledger into dollars. OpenAI publishes no pricing endpoint, so the list is kept here and
 * the spend page shows how far the estimate has drifted from what was actually charged.
 */
@Getter
@Setter
@Validated
@ConfigurationProperties(prefix = "openai")
@FieldDefaults(level = PRIVATE)
public class OpenAiProperties {
    /** An Admin API key from the organisation settings; a project key cannot read costs. */
    @NotBlank
    String adminKey;

    @NotBlank
    String costsUrl = "https://api.openai.com/v1/organization/costs";

    /** The bill lags by hours and the endpoint is rate limited, so one fetch serves the page for this long. */
    @NotNull
    Duration costsCacheTtl = Duration.ofHours(1);

    /** Dollars per million tokens, keyed by the model name the completion reported. */
    @NotNull
    @Valid
    Map<String, ModelPrice> pricing = new LinkedHashMap<>();

    @Getter
    @Setter
    @FieldDefaults(level = PRIVATE)
    public static class ModelPrice {
        @NotNull
        @PositiveOrZero
        BigDecimal input;

        @NotNull
        @PositiveOrZero
        BigDecimal cachedInput;

        @NotNull
        @PositiveOrZero
        BigDecimal output;
    }
}
