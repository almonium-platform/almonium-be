package com.almonium.infra.chat.dto.request;

import com.almonium.analyzer.translator.model.enums.Language;
import jakarta.validation.constraints.NotBlank;

/** A post to a broadcast channel: the app-wide one when no language is given, else that room's. */
public record AnnouncementRequest(Language language, @NotBlank String text) {}
