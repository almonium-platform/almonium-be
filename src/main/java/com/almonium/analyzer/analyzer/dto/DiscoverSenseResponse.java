package com.almonium.analyzer.analyzer.dto;

import java.util.List;

public record DiscoverSenseResponse(
        int index, String headword, String partOfSpeech, String transcription, List<String> translations) {}
