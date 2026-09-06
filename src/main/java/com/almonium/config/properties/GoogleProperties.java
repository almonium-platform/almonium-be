package com.almonium.config.properties;

import static lombok.AccessLevel.PRIVATE;

import com.google.cloud.translate.v3.LocationName;
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
    String projectId;

    @NotBlank
    String parentLocation;

    @NotBlank
    String serviceAccountKeyBase64;

    public LocationName getLocationName() {
        return LocationName.of(projectId, parentLocation);
    }
}
