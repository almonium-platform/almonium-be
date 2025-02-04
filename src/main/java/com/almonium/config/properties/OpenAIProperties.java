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
@ConfigurationProperties(prefix = "openai")
@FieldDefaults(level = PRIVATE)
public class OpenAIProperties {

    @NotNull
    @Valid
    @NestedConfigurationProperty
    GptProperties gpt = new GptProperties();

    @Getter
    @Setter
    @FieldDefaults(level = PRIVATE)
    public static class GptProperties {
        @NotBlank
        String model;

        @NotBlank
        String url;

        @NotBlank
        String key;
    }
}
