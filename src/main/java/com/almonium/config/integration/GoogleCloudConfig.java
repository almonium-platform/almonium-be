package com.almonium.config.integration;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.config.properties.GoogleProperties;
import com.google.api.gax.core.CredentialsProvider;
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.texttospeech.v1.TextToSpeechClient;
import com.google.cloud.texttospeech.v1.TextToSpeechSettings;
import com.google.cloud.translate.v3.LocationName;
import com.google.cloud.translate.v3.TranslationServiceClient;
import com.google.cloud.translate.v3.TranslationServiceSettings;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class GoogleCloudConfig {
    GoogleProperties googleProperties;

    @Bean
    @SneakyThrows(IOException.class)
    public GoogleCredentials googleCredentials() {
        byte[] decodedServiceAccountKey = Base64.getDecoder().decode(googleProperties.getServiceAccountKeyBase64());
        InputStream serviceAccountStream = new ByteArrayInputStream(decodedServiceAccountKey);
        return GoogleCredentials.fromStream(serviceAccountStream);
    }

    @Bean
    public CredentialsProvider googleCredentialsProvider(GoogleCredentials credentials) {
        return FixedCredentialsProvider.create(credentials);
    }

    @Bean
    @SneakyThrows(IOException.class)
    public TranslationServiceClient translationServiceClient(CredentialsProvider credentialsProvider) {
        TranslationServiceSettings settings = TranslationServiceSettings.newBuilder()
                .setCredentialsProvider(credentialsProvider)
                .build();
        return TranslationServiceClient.create(settings);
    }

    @Bean
    @SneakyThrows(IOException.class)
    public TextToSpeechClient textToSpeechClient(CredentialsProvider credentialsProvider) {
        TextToSpeechSettings settings = TextToSpeechSettings.newBuilder()
                .setCredentialsProvider(credentialsProvider)
                .build();
        return TextToSpeechClient.create(settings);
    }

    @Bean
    public LocationName locationName() {
        return googleProperties.getLocationName();
    }
}
