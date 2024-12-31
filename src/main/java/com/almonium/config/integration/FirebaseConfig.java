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
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("!test")
@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class FirebaseConfig {
    GoogleProperties googleProperties;

    @PostConstruct
    public void initializeFirebase() throws IOException {
        byte[] decodedServiceAccountKey =
                Base64.getDecoder().decode(googleProperties.getFirebase().getServiceAccountKeyBase64());
        InputStream serviceAccount = new ByteArrayInputStream(decodedServiceAccountKey);

        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setStorageBucket(googleProperties.getFirebase().getStorage().getBucket())
                .build();

        FirebaseApp.initializeApp(options);
    }
}
