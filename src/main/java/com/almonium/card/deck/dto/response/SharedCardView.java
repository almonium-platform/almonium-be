package com.almonium.card.deck.dto.response;

import com.almonium.analyzer.translator.model.enums.Language;

public record SharedCardView(Language language, SharedWordDto word, SharerDto sharer) {}
