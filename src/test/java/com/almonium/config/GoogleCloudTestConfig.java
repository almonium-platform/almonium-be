package com.almonium.config;

import com.google.cloud.texttospeech.v1.TextToSpeechClient;
import com.google.cloud.translate.v3.LocationName;
import com.google.cloud.translate.v3.TranslateTextResponse;
import com.google.cloud.translate.v3.Translation;
import com.google.cloud.translate.v3.TranslationServiceClient;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@TestConfiguration
public class GoogleCloudTestConfig {

    @Bean
    @Primary
    public TranslationServiceClient mockTranslationServiceClient() {
        TranslationServiceClient mockClient = Mockito.mock(TranslationServiceClient.class);

        // --- Define mock behavior ---
        // Example: Make bulkTranslateText return the input text suffixed with "-translated"
        Mockito.when(mockClient.translateText(Mockito.any(com.google.cloud.translate.v3.TranslateTextRequest.class)))
                .thenAnswer(invocation -> {
                    com.google.cloud.translate.v3.TranslateTextRequest request = invocation.getArgument(0);
                    String originalText = request.getContentsList().isEmpty()
                            ? ""
                            : request.getContentsList().get(0);
                    Translation translation = Translation.newBuilder()
                            .setTranslatedText(originalText + "-translated") // Customize mock response
                            .build();
                    return TranslateTextResponse.newBuilder()
                            .addTranslations(translation)
                            .build();
                });

        // Add more Mockito.when(...).thenReturn(...) setups as needed for your tests

        return mockClient;
    }

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

    // Provide a mock/dummy LocationName (doesn't need much mocking usually)
    @Bean
    @Primary
    public LocationName mockLocationName() {
        // Provide a realistic dummy if needed by any code, otherwise null or basic mock works
        // return Mockito.mock(LocationName.class);
        return LocationName.of("test-project", "global"); // Or return a dummy instance
    }

    // --- NO Credentials Beans Needed ---
    // You don't need to provide mock GoogleCredentials or CredentialsProvider
    // because the mock clients don't use them.
}
