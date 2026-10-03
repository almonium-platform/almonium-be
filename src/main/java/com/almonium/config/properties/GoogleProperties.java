package com.almonium.config.properties;

import static lombok.AccessLevel.PRIVATE;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Validated
@ConfigurationProperties(prefix = "google")
@FieldDefaults(level = PRIVATE)
public class GoogleProperties {

    @NotBlank
    String parentLocation;

    @NotBlank
    String serviceAccountKeyBase64;
}
