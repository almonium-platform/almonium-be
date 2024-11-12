package com.almonium.analyzer.translator.service;

import com.google.protobuf.ByteString;

public interface TranslationService {
    ByteString textToSpeech(String languageCode, String text);

    String bulkTranslateText(String text, String code);
}
