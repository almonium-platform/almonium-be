package com.almonium.config.integration;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("!test")
public class FirebaseConfig {

    @Value(value = "${firebase.storage.bucket}")
    private String storageBucket;

    @Value(value = "${firebase.service-account-key-base64}")
    private String serviceAccountKeyBase64;

    @PostConstruct
    public void initializeFirebase() throws IOException {
        byte[] decodedServiceAccountKey = Base64.getDecoder().decode(serviceAccountKeyBase64);
        InputStream serviceAccount = new ByteArrayInputStream(decodedServiceAccountKey);

        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setStorageBucket(storageBucket)
                .build();

        FirebaseApp.initializeApp(options);
    }
}
