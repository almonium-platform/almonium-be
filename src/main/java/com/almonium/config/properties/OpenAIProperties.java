package com.almonium.config.properties;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

@Getter
@Setter
@ConfigurationProperties(prefix = "openai")
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class OpenAIProperties {

    @NestedConfigurationProperty
    GptProperties gpt = new GptProperties();

    @Getter
    @Setter
    @FieldDefaults(level = lombok.AccessLevel.PRIVATE)
    public static class GptProperties {
        String model;
        String url;
        String key;
    }
}
