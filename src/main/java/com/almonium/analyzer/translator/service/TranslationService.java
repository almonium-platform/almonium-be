package com.almonium.analyzer.translator.service;

import com.google.protobuf.ByteString;

public interface TranslationService {
    ByteString textToSpeech(VoiceCatalogue.Voice voice, String text);
}
