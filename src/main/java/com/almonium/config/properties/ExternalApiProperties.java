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
@ConfigurationProperties(prefix = "external.api")
@FieldDefaults(level = PRIVATE)
public class ExternalApiProperties {

    @NotNull
    @Valid
    @NestedConfigurationProperty
    Ngrams ngrams = new Ngrams();

    @NotNull
    @Valid
    @NestedConfigurationProperty
    ApiKeys key = new ApiKeys();

    @Getter
    @Setter
    @FieldDefaults(level = PRIVATE)
    public static class Ngrams {
        @NotBlank
        String url;
    }

    @Getter
    @Setter
    @FieldDefaults(level = PRIVATE)
    public static class ApiKeys {
        @NotBlank
        String urban;

        @NotBlank
        String wordnik;

        @NotBlank
        String yandex;

        @NotBlank
        String words;
    }
}
