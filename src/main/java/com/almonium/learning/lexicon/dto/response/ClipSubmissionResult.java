package com.almonium.learning.lexicon.dto.response;

import java.util.UUID;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

/**
 * What became of a submitted clip: the row that stands for it (the same id when the rendering was submitted before),
 * where its bytes are, and whether this submission wrote them. {@code stored} is false when the object already
 * existed, which is the write-once rule at work.
 */
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record ClipSubmissionResult(UUID clipId, String objectKey, boolean stored) {}
