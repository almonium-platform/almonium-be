package com.linguarium.translator.service;

import com.google.protobuf.ByteString;

import java.util.List;

public interface TranslationService {
    ByteString textToSpeech(String languageCode, String text);

    String bulkTranslateText(String text, String code);
}
