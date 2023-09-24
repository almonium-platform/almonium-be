package com.linguatool.service.impl;

import com.google.cloud.texttospeech.v1.*;
import com.google.cloud.translate.v3.*;
import com.google.protobuf.ByteString;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.stream.Collectors;

import static lombok.AccessLevel.PRIVATE;


@Service
@Slf4j
@AllArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class GoogleTranslationService {

    static String projectId = "linguatool";
    static LocationName parent = LocationName.of(projectId, "global");
    TranslationServiceClient translationClient;
    TextToSpeechClient textToSpeechClient;

    @SneakyThrows
    public GoogleTranslationService() {
        translationClient = TranslationServiceClient.create();
        textToSpeechClient = TextToSpeechClient.create();
    }

    @SneakyThrows
    public ByteString textToSpeech(String languageCode, String text) {
        SynthesisInput input = SynthesisInput.newBuilder().setText(text).build();

        // Build the voice request, select the language code ("en-US") and the ssml voice gender
        // ("neutral")
        VoiceSelectionParams voice =
                VoiceSelectionParams.newBuilder()
                        .setLanguageCode(languageCode)
                        .setSsmlGender(SsmlVoiceGender.NEUTRAL)
                        .build();

        // Select the type of audio file you want returned
        AudioConfig audioConfig =
                AudioConfig.newBuilder().setAudioEncoding(AudioEncoding.MP3).build();

        // Perform the text-to-speech request on the text input with the selected voice parameters and
        // audio file type
        SynthesizeSpeechResponse response =
                textToSpeechClient.synthesizeSpeech(input, voice, audioConfig);

        // Get the audio contents from the response
        return response.getAudioContent();
    }

    public List<String> translateText(String text, String targetLanguage) {

        TranslateTextRequest request =
                TranslateTextRequest.newBuilder()
                        .setParent(parent.toString())
                        .setMimeType("text/plain")
                        .setTargetLanguageCode(targetLanguage)
                        .addContents(text)
                        .build();

        TranslateTextResponse response = translationClient.translateText(request);

        return response
                .getTranslationsList()
                .stream()
                .map(Translation::getTranslatedText)
                .collect(Collectors.toList());
    }

    public String bulkTranslateText(String text, String targetLanguage) {

        TranslateTextRequest request =
                TranslateTextRequest.newBuilder()
                        .setParent(parent.toString())
                        .setMimeType("text/plain")
                        .setTargetLanguageCode(targetLanguage)
                        .addContents(text)
                        .build();

        TranslateTextResponse response = translationClient.translateText(request);

        return response.getTranslationsList().get(0).getTranslatedText();
    }
}
