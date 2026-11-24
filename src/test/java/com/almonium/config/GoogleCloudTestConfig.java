package com.almonium.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.texttospeech.v1.TextToSpeechClient;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@TestConfiguration
public class GoogleCloudTestConfig {

    // Provide a mock TextToSpeechClient
    @Bean
    @Primary
    public TextToSpeechClient mockTextToSpeechClient() {
        TextToSpeechClient mockClient = Mockito.mock(TextToSpeechClient.class);
        // Define mock behavior if needed, e.g., return dummy ByteString
        // Mockito.when(mockClient.synthesizeSpeech(Mockito.any(), Mockito.any(), Mockito.any()))
        //
        // .thenReturn(SynthesizeSpeechResponse.newBuilder().setAudioContent(ByteString.copyFromUtf8("dummy-audio")).build());
        return mockClient;
    }

    @Bean
    @Primary
    public GoogleCredentials mockGoogleCredentials() {
        return Mockito.mock(GoogleCredentials.class);
    }
}
