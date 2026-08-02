package com.almonium.config.security;

import static jakarta.ws.rs.HttpMethod.DELETE;
import static jakarta.ws.rs.HttpMethod.GET;
import static jakarta.ws.rs.HttpMethod.OPTIONS;
import static jakarta.ws.rs.HttpMethod.PATCH;
import static jakarta.ws.rs.HttpMethod.POST;
import static jakarta.ws.rs.HttpMethod.PUT;
import static jakarta.ws.rs.core.HttpHeaders.AUTHORIZATION;
import static jakarta.ws.rs.core.HttpHeaders.CACHE_CONTROL;
import static jakarta.ws.rs.core.HttpHeaders.CONTENT_TYPE;
import static lombok.AccessLevel.PRIVATE;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

import com.almonium.auth.firebase.filter.FirebaseSessionAuthenticationFilter;
import com.almonium.auth.firebase.security.FirebaseBearerToken;
import com.almonium.config.properties.AppProperties;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.security.web.csrf.CsrfTokenRequestHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class WebSecurityConfig {
    FirebaseSessionAuthenticationFilter firebaseSessionAuthenticationFilter;
    AppProperties appProperties;

    private static final String[] PUBLIC_URL_PATTERNS = new String[] {
        // Swagger
        "/swagger-ui/**",
        "/v3/api-docs/**",
        // Public endpoints
        "/public/**",
        // Actuator
        "/actuator/health/**",
        "/actuator/info"
    };

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of(appProperties.getWebDomain()));
        configuration.setAllowedMethods(List.of(GET, POST, PUT, PATCH, DELETE, OPTIONS));
        configuration.setAllowedHeaders(
                List.of(CONTENT_TYPE, AUTHORIZATION, CACHE_CONTROL, "X-XSRF-TOKEN", "ngsw-bypass"));
        configuration.setAllowCredentials(true); // `withCredentials: true` won't work without this
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public CsrfTokenRepository csrfTokenRepository() {
        CookieCsrfTokenRepository repository = CookieCsrfTokenRepository.withHttpOnlyFalse();
        repository.setCookieCustomizer(b -> {
            b.path("/");
            String host = URI.create(appProperties.getApiDomain()).getHost();
            if (host != null && !host.equalsIgnoreCase("localhost")) {
                b.domain("almonium.com");
                b.secure(true);
                b.sameSite("Lax");
            }
        });
        return repository;
    }

    @Bean
    public CsrfTokenRequestHandler csrfTokenRequestHandler() {
        CsrfTokenRequestAttributeHandler requestHandler = new CsrfTokenRequestAttributeHandler();
        requestHandler.setCsrfRequestAttributeName(null);
        return requestHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http, CsrfTokenRepository csrfTokenRepository, CsrfTokenRequestHandler csrfTokenRequestHandler)
            throws Exception {

        return http.csrf(csrf -> csrf.csrfTokenRepository(csrfTokenRepository)
                        .csrfTokenRequestHandler(csrfTokenRequestHandler)
                        .ignoringRequestMatchers(
                                new AntPathRequestMatcher("/public/**"),
                                new AntPathRequestMatcher("/internal/books/publications"),
                                new AntPathRequestMatcher("/internal/books/import-events"),
                                new AntPathRequestMatcher("/actuator/**"),
                                request -> FirebaseBearerToken.from(request).isPresent()))
                .cors(Customizer.withDefaults())
                .exceptionHandling((exception) ->
                        exception.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
                .authorizeHttpRequests(auth -> auth.requestMatchers(PUBLIC_URL_PATTERNS)
                        .permitAll()
                        .requestMatchers("/internal/books/publications", "/internal/books/import-events")
                        .permitAll()
                        .requestMatchers("/auth/session", "/auth/session/logout")
                        .permitAll()
                        .anyRequest()
                        .authenticated())
                .sessionManagement(manager -> manager.sessionCreationPolicy(STATELESS))
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .addFilterBefore(firebaseSessionAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
