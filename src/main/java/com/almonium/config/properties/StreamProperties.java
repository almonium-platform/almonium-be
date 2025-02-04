package com.almonium.config.properties;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "stream.api")
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class StreamProperties {
    String key;
    String secret;
}