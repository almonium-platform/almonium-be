package com.almonium.config.properties;

import static lombok.AccessLevel.PRIVATE;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
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
    }

    @Getter
    @Setter
    @FieldDefaults(level = PRIVATE)
    public static class Auth {
        boolean emailVerificationRequired;

        @NotNull
        @Valid
        @NestedConfigurationProperty
        VerificationToken verificationToken = new VerificationToken();

        @NotNull
        @Valid
        @NestedConfigurationProperty
        Jwt jwt = new Jwt();

        @NotNull
        @Valid
        @NestedConfigurationProperty
        Oauth2 oauth2 = new Oauth2();

        @Getter
        @Setter
        @FieldDefaults(level = PRIVATE)
        public static class VerificationToken {

            @Min(1)
            int lifetime;

            @Min(1)
            int length;
        }

        @Getter
        @Setter
        @FieldDefaults(level = PRIVATE)
        public static class Jwt {

            @NotBlank
            String secret;

            @NotNull
            @Valid
            @NestedConfigurationProperty
            AccessToken accessToken = new AccessToken();

            @NotNull
            @Valid
            @NestedConfigurationProperty
            RefreshToken refreshToken = new RefreshToken();

            @Getter
            @Setter
            @FieldDefaults(level = PRIVATE)
            public static class AccessToken {
                @Min(1)
                int lifetime;
            }

            @Getter
            @Setter
            @FieldDefaults(level = PRIVATE)
            public static class RefreshToken {

                @Min(1)
                int lifetime;

                @NotBlank
                String url;

                @NotBlank
                String fullUrl;
            }
        }

        @Getter
        @Setter
        @FieldDefaults(level = PRIVATE)
        public static class Oauth2 {

            @NotEmpty
            List<@NotBlank String> authorizedRedirectUris;

            @NotBlank
            String appleTokenUrl;

            @NotBlank
            String appleServiceId;
        }
    }
}
