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

@Getter
@Setter
@Validated
@ConfigurationProperties(prefix = "app")
@FieldDefaults(level = PRIVATE)
public class AppProperties {

    @NotBlank
    String name;

    @NotBlank
    String webDomain;

    @NotBlank
    String apiDomain;

    @NotNull
    @Valid
    @NestedConfigurationProperty
    Email email = new Email();

    @NotNull
    @Valid
    @NestedConfigurationProperty
    Auth auth = new Auth();

    @Getter
    @Setter
    @FieldDefaults(level = PRIVATE)
    public static class Email {
        boolean dryRun;

        @NotBlank
        String apiUrl;

        @NotBlank
        String apiKey;

        @NotBlank
        String fromAddress;

        @NotBlank
        String fromName;
    }

    @Getter
    @Setter
    @FieldDefaults(level = PRIVATE)
    public static class Auth {
        @NotNull
        @Valid
        @NestedConfigurationProperty
        Firebase firebase = new Firebase();

        @Getter
        @Setter
        @FieldDefaults(level = PRIVATE)
        public static class Firebase {
            @Min(60)
            int sessionLifetimeSeconds;

            @Min(1)
            int recentLoginSeconds;
        }
    }
}
