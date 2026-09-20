package com.almonium.analyzer.translator.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.almonium.analyzer.translator.model.enums.LanguageVariety;
import com.almonium.analyzer.translator.service.VoiceCatalogue;
import com.google.cloud.texttospeech.v1.*;
import com.google.protobuf.ByteString;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

class GoogleVoiceSelectionTest {
    @Test
    void sendsTheExactConfiguredVoiceToGoogle() throws Exception {
        TextToSpeechClient client = mock(TextToSpeechClient.class);
        when(client.synthesizeSpeech(
                        any(SynthesisInput.class), any(VoiceSelectionParams.class), any(AudioConfig.class)))
                .thenReturn(SynthesizeSpeechResponse.newBuilder()
                        .setAudioContent(ByteString.copyFromUtf8("audio"))
                        .build());
        var adapter = new GoogleTranslationServiceImpl(null, client, null);
        var route = new VoiceCatalogue().requireVoice(LanguageVariety.EN_GB);
        assertThat(adapter.textToSpeech(route, "schedule").toStringUtf8()).isEqualTo("audio");
        var voice = ArgumentCaptor.forClass(VoiceSelectionParams.class);
        verify(client).synthesizeSpeech(any(SynthesisInput.class), voice.capture(), any(AudioConfig.class));
        assertThat(voice.getValue().getLanguageCode()).isEqualTo("en-GB");
        assertThat(voice.getValue().getName()).isEqualTo(route.voiceId());
    }
}
