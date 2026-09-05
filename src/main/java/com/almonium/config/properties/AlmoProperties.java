package com.almonium.config.properties;

import static lombok.AccessLevel.PRIVATE;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.validation.annotation.Validated;

/**
 * 11: the shape of the one premium line generated per request. Everything that costs money is a number here rather
 * than a constant in the service, so the ceiling and the model can move without a release.
 */
@Getter
@Setter
@Validated
@ConfigurationProperties(prefix = "almo")
@FieldDefaults(level = PRIVATE)
public class AlmoProperties {
    /** The Stream user Almo speaks as. It has no row in our database, like the app's own account. */
    @NotBlank
    String userId = "almo";

    /** The most queue words a system prompt is built from: not the whole deck. */
    @Min(1)
    int promptWords = 40;

    /** How many recent messages of the thread are sent along with each turn. */
    @Min(1)
    int historySize = 20;

    /** Soft ceiling on Almo's replies per user per rolling day, enforced here and not surfaced by the client. */
    @Min(1)
    int dailyMessageCeiling = 60;

    @NotNull
    @Valid
    @NestedConfigurationProperty
    OpenAi openAi = new OpenAi();

    @Getter
    @Setter
    @FieldDefaults(level = PRIVATE)
    public static class OpenAi {
        /** Absent means the channel exists but cannot answer; the app still boots. */
        String apiKey;

        @NotBlank
        String model = "gpt-4.1-mini";

        @NotBlank
        String url = "https://api.openai.com/v1/chat/completions";

        @Min(1)
        int maxOutputTokens = 500;
    }
}
