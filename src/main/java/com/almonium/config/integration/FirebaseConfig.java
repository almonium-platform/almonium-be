package com.almonium.config.integration;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.config.properties.GoogleProperties;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Slf4j
@Profile("!test")
@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class FirebaseConfig {
    GoogleProperties googleProperties;

    @Bean
    public FirebaseApp firebaseApp(GoogleCredentials credentials) {
        FirebaseOptions.Builder builder = FirebaseOptions.builder().setCredentials(credentials);

        // The SDK resolves a project id from the credentials for its own calls but never writes it
        // back, so FirebaseOptions reports only what was set here. Anything that needs to know which
        // project it is talking to - naming it before a destructive action, say - reads it from the
        // options and would otherwise find nothing.
        projectIdOf(credentials).ifPresent(builder::setProjectId);
        FirebaseOptions options = builder.build();

        if (FirebaseApp.getApps().isEmpty()) {
            log.info("Initializing FirebaseApp...");
            return FirebaseApp.initializeApp(options);
        }
        log.warn("FirebaseApp already initialized, returning existing default app.");
        return FirebaseApp.getInstance();
    }

    @Bean
    public FirebaseAuth firebaseAuth(FirebaseApp firebaseApp) {
        return FirebaseAuth.getInstance(firebaseApp);
    }

    private Optional<String> projectIdOf(GoogleCredentials credentials) {
        return credentials instanceof ServiceAccountCredentials serviceAccount
                ? Optional.ofNullable(serviceAccount.getProjectId())
                : Optional.empty();
    }
}
