package com.almonium.user.core.dto.request;

import com.almonium.analyzer.analyzer.model.enums.CEFR;

public record UpdateLearnerRequest(Boolean active, CEFR level) {}
