package com.almonium.config.integration;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.config.properties.GoogleProperties;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
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
    @Qualifier("firebaseCredentials")
    public GoogleCredentials firebaseCredentials() throws IOException {
        byte[] decodedServiceAccountKey =
                Base64.getDecoder().decode(googleProperties.getFirebase().getServiceAccountKeyBase64());
        InputStream serviceAccountStream = new ByteArrayInputStream(decodedServiceAccountKey);
        return GoogleCredentials.fromStream(serviceAccountStream);
    }

    @Bean
    public FirebaseApp firebaseApp(@Qualifier("firebaseCredentials") GoogleCredentials credentials) throws IOException {
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
