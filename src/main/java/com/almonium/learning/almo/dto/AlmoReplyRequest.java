package com.almonium.learning.almo.dto;

import jakarta.validation.constraints.NotBlank;

/** The learner's message Almo is asked to answer. */
public record AlmoReplyRequest(@NotBlank String userMessageId) {}
