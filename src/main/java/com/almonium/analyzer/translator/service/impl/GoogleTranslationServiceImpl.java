package com.almonium.analyzer.translator.service.impl;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.analyzer.translator.service.TranslationService;
import com.almonium.analyzer.translator.service.VoiceCatalogue;
import com.google.cloud.texttospeech.v1.AudioConfig;
import com.google.cloud.texttospeech.v1.AudioEncoding;
import com.google.cloud.texttospeech.v1.SynthesisInput;
import com.google.cloud.texttospeech.v1.SynthesizeSpeechResponse;
import com.google.cloud.texttospeech.v1.TextToSpeechClient;
import com.google.cloud.texttospeech.v1.VoiceSelectionParams;
import com.google.protobuf.ByteString;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@FieldDefaults(level = PRIVATE)
public class GoogleTranslationServiceImpl implements TranslationService {
    TextToSpeechClient textToSpeechClient;

    @SneakyThrows
    @Override
    public ByteString textToSpeech(VoiceCatalogue.Voice selectedVoice, String text) {
        SynthesisInput input = SynthesisInput.newBuilder().setText(text).build();
        VoiceSelectionParams voice = VoiceSelectionParams.newBuilder()
                .setLanguageCode(selectedVoice.languageCode())
                .setName(selectedVoice.voiceId())
                .build();
        AudioConfig audioConfig =
                AudioConfig.newBuilder().setAudioEncoding(AudioEncoding.MP3).build();
        SynthesizeSpeechResponse response = textToSpeechClient.synthesizeSpeech(input, voice, audioConfig);
        return response.getAudioContent();
    }
}
