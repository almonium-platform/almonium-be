package linguarium.config.security;

import static jakarta.ws.rs.HttpMethod.DELETE;
import static jakarta.ws.rs.HttpMethod.GET;
import static jakarta.ws.rs.HttpMethod.OPTIONS;
import static jakarta.ws.rs.HttpMethod.POST;
import static jakarta.ws.rs.HttpMethod.PUT;
import static jakarta.ws.rs.core.HttpHeaders.AUTHORIZATION;
import static jakarta.ws.rs.core.HttpHeaders.CACHE_CONTROL;
import static jakarta.ws.rs.core.HttpHeaders.CONTENT_TYPE;
import static lombok.AccessLevel.PRIVATE;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

import java.util.List;
import linguarium.auth.oauth2.OAuth2CookieRequestRepository;
import linguarium.auth.oauth2.handler.OAuth2AuthenticationFailureHandler;
import linguarium.auth.oauth2.handler.OAuth2AuthenticationSuccessHandler;
import linguarium.auth.oauth2.service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
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
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
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
    CustomOAuth2UserService customOAuth2UserService;
    OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;
    OAuth2CookieRequestRepository authorizationRequestRepository;

    private static final List<String> PERMIT_ALL_URL_PATTERNS =
            List.of("/auth/**", "swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**");

    @NonFinal
    @Value("${app.server.frontend.url}")
    String frontendUrl;

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
        configuration.setAllowedOrigins(List.of(frontendUrl));
        configuration.setAllowedMethods(List.of(GET, POST, PUT, DELETE, OPTIONS));
        configuration.setAllowedHeaders(List.of(CONTENT_TYPE, AUTHORIZATION, CACHE_CONTROL));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        return http.csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .exceptionHandling((exception) ->
                        exception.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.NOT_FOUND)))
                .authorizeHttpRequests(auth -> auth.requestMatchers(PERMIT_ALL_URL_PATTERNS.toArray(String[]::new))
                        .permitAll()
                        .anyRequest()
                        .authenticated())
                .sessionManagement(manager -> manager.sessionCreationPolicy(STATELESS))
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .oauth2Login(loginConfigurer -> loginConfigurer
                        .userInfoEndpoint(endpointConfig -> endpointConfig.userService(customOAuth2UserService))
                        .successHandler(oAuth2AuthenticationSuccessHandler)
                        .authorizationEndpoint(authEndPoint ->
                                authEndPoint.authorizationRequestRepository(authorizationRequestRepository))
                        .failureHandler(oAuth2AuthenticationFailureHandler))
                .build();
    }
}
