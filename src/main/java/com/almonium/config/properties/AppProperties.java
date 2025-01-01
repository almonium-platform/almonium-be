package com.almonium.config.properties;

import static lombok.AccessLevel.PRIVATE;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

@Getter
@Setter
@ConfigurationProperties(prefix = "app")
@FieldDefaults(level = PRIVATE)
public class AppProperties {
    String name;
    String webDomain;
    String apiDomain;

    @NestedConfigurationProperty
    Email email = new Email();

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

        @NestedConfigurationProperty
        VerificationToken verificationToken = new VerificationToken();

        @NestedConfigurationProperty
        Jwt jwt = new Jwt();

        @NestedConfigurationProperty
        Oauth2 oauth2 = new Oauth2();

        @Getter
        @Setter
        @FieldDefaults(level = PRIVATE)
        public static class VerificationToken {
            int lifetime;
            int length;
        }

        @Getter
        @Setter
        @FieldDefaults(level = PRIVATE)
        public static class Jwt {
            String secret;

            @NestedConfigurationProperty
            AccessToken accessToken = new AccessToken();

            @NestedConfigurationProperty
            RefreshToken refreshToken = new RefreshToken();

            @Getter
            @Setter
            @FieldDefaults(level = PRIVATE)
            public static class AccessToken {
                int lifetime;
            }

            @Getter
            @Setter
            @FieldDefaults(level = PRIVATE)
            public static class RefreshToken {
                int lifetime;
                String url;
                String fullUrl;
            }
        }

        @Getter
        @Setter
        @FieldDefaults(level = PRIVATE)
        public static class Oauth2 {
            List<String> authorizedRedirectUris;
            String appleTokenUrl;
            String appleServiceId;
        }
    }
}
