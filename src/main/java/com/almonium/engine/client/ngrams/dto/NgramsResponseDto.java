package com.almonium.engine.client.ngrams.dto;

import java.util.List;

public record NgramsResponseDto(List<Ngram> ngrams) {
    public record Ngram(double relTotalMatchCount) {
    }
}
