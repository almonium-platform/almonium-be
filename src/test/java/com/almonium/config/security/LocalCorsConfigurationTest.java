package com.almonium.config.security;

import static org.assertj.core.api.Assertions.assertThat;

import com.almonium.config.properties.AppProperties;
import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.context.properties.source.ConfigurationPropertySources;
import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.cors.CorsConfiguration;

@DisplayName("The local profile lets a browser on any local port reach the API")
class LocalCorsConfigurationTest {

    private static final CorsConfiguration LOCAL_CORS = localCorsConfiguration();

    @ParameterizedTest
    @ValueSource(
            strings = {
                "http://localhost:9999",
                "http://localhost:4200",
                "http://localhost:53127",
                "https://localhost:4200",
                "http://127.0.0.1:8080",
                "https://127.0.0.1:8080",
                "http://[::1]:5173"
            })
    void allowsEveryLocalOrigin(String origin) {
        assertThat(LOCAL_CORS.checkOrigin(origin)).isEqualTo(origin);
    }

    @ParameterizedTest
    @ValueSource(strings = {"https://almonium.com", "http://localhost.attacker.com", "http://notlocalhost:4200"})
    void stillRejectsOriginsThatOnlyLookLocal(String origin) {
        assertThat(LOCAL_CORS.checkOrigin(origin)).isNull();
    }

    @DisplayName("Credentials stay allowed, since the browser client authenticates with a session cookie")
    @ParameterizedTest
    @ValueSource(strings = "http://localhost:4200")
    void keepsCredentialsAllowed(String origin) {
        assertThat(LOCAL_CORS.checkOrigin(origin)).isEqualTo(origin);
        assertThat(LOCAL_CORS.getAllowCredentials()).isTrue();
    }

    @DisplayName("A deployed profile configures no patterns, so only its own web domain is allowed")
    @Test
    void withoutConfiguredPatternsOnlyTheWebDomainIsAllowed() {
        AppProperties deployed = new AppProperties();
        deployed.setWebDomain("https://almonium.com");
        CorsConfiguration configuration = corsConfiguration(deployed);

        assertThat(configuration.checkOrigin("https://almonium.com")).isEqualTo("https://almonium.com");
        assertThat(configuration.checkOrigin("http://localhost:4200")).isNull();
    }

    private static CorsConfiguration localCorsConfiguration() {
        return corsConfiguration(localProperties());
    }

    private static CorsConfiguration corsConfiguration(AppProperties properties) {
        CorsConfiguration configuration = new WebSecurityConfig(null, properties)
                .corsConfigurationSource()
                .getCorsConfiguration(new MockHttpServletRequest("GET", "/users/me"));
        assertThat(configuration).isNotNull();
        return configuration;
    }

    private static AppProperties localProperties() {
        try {
            List<PropertySource<?>> sources =
                    new YamlPropertySourceLoader().load("local", new ClassPathResource("application-local.yaml"));
            return new Binder(ConfigurationPropertySources.from(sources))
                    .bind("app", AppProperties.class)
                    .get();
        } catch (IOException e) {
            throw new IllegalStateException("application-local.yaml is unreadable", e);
        }
    }
}
