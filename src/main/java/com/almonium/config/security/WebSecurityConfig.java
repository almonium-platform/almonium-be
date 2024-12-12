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

import com.almonium.auth.oauth2.apple.filter.AppleOidcUserFilter;
import com.almonium.auth.oauth2.other.handler.OAuth2AuthenticationFailureHandler;
import com.almonium.auth.oauth2.other.handler.OAuth2AuthenticationSuccessHandler;
import com.almonium.auth.oauth2.other.repository.OAuth2CookieRequestRepository;
import com.almonium.auth.oauth2.other.service.OAuth2UserDetailsService;
import com.almonium.auth.token.filter.TokenAuthenticationFilter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class WebSecurityConfig {
    UserDetailsService userDetailsService;
    PasswordEncoder passwordEncoder;
    OAuth2UserDetailsService OAuth2UserDetailsService;
    TokenAuthenticationFilter tokenAuthenticationFilter;
    AppleOidcUserFilter appleOidcUserFilter;
    OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;
    OAuth2CookieRequestRepository authorizationRequestRepository;

    @NonFinal
    @Value("${app.web-domain}")
    String domain;

    @NonFinal
    @Value("${app.auth.oauth2.apple-token-url}")
    String appleTokenUrl;

    private static final String[] PUBLIC_URL_PATTERNS = new String[] {
        // Swagger
        "/swagger-ui/**",
        "/v3/api-docs/**",
        // OAuth2
        "/oauth2/authorization/**",
        // Public endpoints
        "/public/**",
    };

    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);

        return new ProviderManager(authenticationProvider);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of(domain, appleTokenUrl));
        configuration.setAllowedMethods(List.of(GET, POST, PUT, PATCH, DELETE, OPTIONS));
        configuration.setAllowedHeaders(List.of(CONTENT_TYPE, AUTHORIZATION, CACHE_CONTROL));
        configuration.setAllowCredentials(true); // `withCredentials: true` won't work without this
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    @SneakyThrows
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {

        return http.csrf(AbstractHttpConfigurer::disable) // to enable
                .cors(Customizer.withDefaults())
                .exceptionHandling((exception) ->
                        exception.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
                .authorizeHttpRequests(auth -> auth.requestMatchers(PUBLIC_URL_PATTERNS)
                        .permitAll()
                        .anyRequest()
                        .authenticated())
                .sessionManagement(manager -> manager.sessionCreationPolicy(STATELESS))
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .addFilterBefore(tokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(appleOidcUserFilter, OAuth2LoginAuthenticationFilter.class)
                .oauth2Login(loginConfigurer -> loginConfigurer
                        .userInfoEndpoint(endpointConfig -> endpointConfig.userService(OAuth2UserDetailsService))
                        .successHandler(oAuth2AuthenticationSuccessHandler)
                        .authorizationEndpoint(authEndpoint ->
                                authEndpoint.authorizationRequestRepository(authorizationRequestRepository))
                        .failureHandler(oAuth2AuthenticationFailureHandler))
                .build();
    }
}
