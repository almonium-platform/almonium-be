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
import com.almonium.config.properties.AppProperties;
import com.almonium.config.security.filter.CsrfCookieFilter;
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
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class WebSecurityConfig {
    OAuth2UserDetailsService OAuth2UserDetailsService;

    TokenAuthenticationFilter tokenAuthenticationFilter;
    CsrfCookieFilter csrfCookieFilter;
    AppleOidcUserFilter appleOidcUserFilter;

    OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;

    OAuth2CookieRequestRepository authorizationRequestRepository;
    AppProperties appProperties;

    private static final String[] PUBLIC_URL_PATTERNS = new String[] {
        // Swagger
        "/swagger-ui/**",
        "/v3/api-docs/**",
        // OAuth2
        "/oauth2/authorization/**",
        // Public endpoints
        "/public/**",
        // Actuator
        "/actuator/health/**",
        "/actuator/info"
    };

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of(
                appProperties.getWebDomain(),
                appProperties.getAuth().getOauth2().getAppleTokenUrl()));
        configuration.setAllowedMethods(List.of(GET, POST, PUT, PATCH, DELETE, OPTIONS));
        configuration.setAllowedHeaders(List.of(CONTENT_TYPE, AUTHORIZATION, CACHE_CONTROL, "X-XSRF-TOKEN"));
        configuration.setAllowCredentials(true); // `withCredentials: true` won't work without this
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        var repo = CookieCsrfTokenRepository.withHttpOnlyFalse();
        repo.setCookieCustomizer(b -> b.path("/"));

        var requestHandler = new CsrfTokenRequestAttributeHandler();
        requestHandler.setCsrfRequestAttributeName("_csrf");

        return http.csrf(csrf -> csrf.csrfTokenRepository(repo).csrfTokenRequestHandler(requestHandler))
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
                .addFilterAfter(csrfCookieFilter, CsrfFilter.class)
                .oauth2Login(loginConfigurer -> loginConfigurer
                        .userInfoEndpoint(endpointConfig -> endpointConfig.userService(OAuth2UserDetailsService))
                        .successHandler(oAuth2AuthenticationSuccessHandler)
                        .authorizationEndpoint(authEndpoint ->
                                authEndpoint.authorizationRequestRepository(authorizationRequestRepository))
                        .failureHandler(oAuth2AuthenticationFailureHandler))
                .build();
    }
}
