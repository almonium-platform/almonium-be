package com.almonium.engine.translator.service.impl;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.engine.translator.service.TranslationService;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
@FieldDefaults(level = PRIVATE)
public class GoogleTranslationServiceImpl implements TranslationService {
    @Value("${google.projectId}")
    String projectId;

    @Value("${google.parentLocation}")
    String parentLocation;

    LocationName parent;

    TranslationServiceClient translationClient;
    TextToSpeechClient textToSpeechClient;

    @SneakyThrows
    public GoogleTranslationServiceImpl() {
        translationClient = TranslationServiceClient.create();
        textToSpeechClient = TextToSpeechClient.create();
    }

    @SneakyThrows
    @Override
    public ByteString textToSpeech(String languageCode, String text) {
        SynthesisInput input = SynthesisInput.newBuilder().setText(text).build();

        // Build the voice request, select the language code ("en-US") and the ssml voice gender
        // ("neutral")
        VoiceSelectionParams voice = VoiceSelectionParams.newBuilder()
                .setLanguageCode(languageCode)
                .setSsmlGender(SsmlVoiceGender.NEUTRAL)
                .build();

        // Select the type of audio file you want returned
        AudioConfig audioConfig =
                AudioConfig.newBuilder().setAudioEncoding(AudioEncoding.MP3).build();

        // Perform the text-to-speech request on the text input with the selected voice parameters and
        // audio file type
        SynthesizeSpeechResponse response = textToSpeechClient.synthesizeSpeech(input, voice, audioConfig);

        // Get the audio contents from the response
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

        // TODO what's in other indexes?
        return response.getTranslationsList().get(0).getTranslatedText();
    }
}
