package com.almonium.config.integration;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.config.properties.GoogleProperties;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
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
        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(credentials)
                .setStorageBucket(googleProperties.getFirebase().getStorage().getBucket())
                .build();

        if (FirebaseApp.getApps().isEmpty()) {
            log.info("Initializing FirebaseApp...");
            return FirebaseApp.initializeApp(options);
        }
        log.warn("FirebaseApp already initialized, returning existing default app.");
        return FirebaseApp.getInstance();
    }
}
