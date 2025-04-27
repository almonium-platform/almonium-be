package com.almonium.analyzer.translator.service.impl;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.analyzer.translator.service.TranslationService;
import com.google.cloud.texttospeech.v1.AudioConfig;
import com.google.cloud.texttospeech.v1.AudioEncoding;
import com.google.cloud.texttospeech.v1.SsmlVoiceGender;
import com.google.cloud.texttospeech.v1.SynthesisInput;
import com.google.cloud.texttospeech.v1.SynthesizeSpeechResponse;
import com.google.cloud.texttospeech.v1.TextToSpeechClient;
import com.google.cloud.texttospeech.v1.VoiceSelectionParams;
import com.google.cloud.translate.v3.LocationName;
import com.google.cloud.translate.v3.TranslateTextRequest;
import com.google.cloud.translate.v3.TranslateTextResponse;
import com.google.cloud.translate.v3.TranslationServiceClient;
import com.google.protobuf.ByteString;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@FieldDefaults(level = PRIVATE)
public class GoogleTranslationServiceImpl implements TranslationService {
    TranslationServiceClient translationClient;
    TextToSpeechClient textToSpeechClient;
    LocationName parent;

    @SneakyThrows
    @Override
    public ByteString textToSpeech(String languageCode, String text) {
        SynthesisInput input = SynthesisInput.newBuilder().setText(text).build();
        VoiceSelectionParams voice = VoiceSelectionParams.newBuilder()
                .setLanguageCode(languageCode)
                .setSsmlGender(SsmlVoiceGender.NEUTRAL)
                .build();
        AudioConfig audioConfig =
                AudioConfig.newBuilder().setAudioEncoding(AudioEncoding.MP3).build();
        SynthesizeSpeechResponse response = textToSpeechClient.synthesizeSpeech(input, voice, audioConfig);
        return response.getAudioContent();
    }

    @Override
    public String bulkTranslateText(String text, String targetLanguage) {
        TranslateTextRequest request = TranslateTextRequest.newBuilder()
                .setParent(parent.toString())
                .setMimeType("text/plain")
                .setTargetLanguageCode(targetLanguage)
                .addContents(text)
                .build();

        TranslateTextResponse response = translationClient.translateText(request);

        if (response.getTranslationsList().isEmpty()) {
            // Handle case where no translation was returned
            System.err.println("Warning: No translation returned for text: " + text);
            return "";
        }
        return response.getTranslationsList().get(0).getTranslatedText();
    }
}
