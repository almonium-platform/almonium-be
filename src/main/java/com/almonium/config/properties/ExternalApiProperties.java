package com.almonium.config.properties;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

@Getter
@Setter
@ConfigurationProperties(prefix = "external.api")
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class ExternalApiProperties {

    @NestedConfigurationProperty
    Ngrams ngrams = new Ngrams();

    @NestedConfigurationProperty
    ApiKeys key = new ApiKeys();

    @Getter
    @Setter
    @FieldDefaults(level = lombok.AccessLevel.PRIVATE)
    public static class Ngrams {
        String url;
    }

    @Getter
    @Setter
    @FieldDefaults(level = lombok.AccessLevel.PRIVATE)
    public static class ApiKeys {
        String urban;
        String wordnik;
        String yandex;
        String words;
    }
}
