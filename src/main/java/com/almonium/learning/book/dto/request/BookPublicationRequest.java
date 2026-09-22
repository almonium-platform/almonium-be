package com.almonium.learning.book.dto.request;

import com.almonium.analyzer.analyzer.model.enums.CEFR;
import com.almonium.analyzer.translator.model.enums.Language;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

/**
 * {@code editionId} is the processor's own id for the edition and {@code externalJobId} the id we handed it when we
 * asked for the work (a library suggestion); both are optional so an older processor can still publish, as is
 * {@code editionNote}, editorial's one sentence about the edition for the book page. {@code adaptsTo} is the work's
 * adaptation floor as the processor found it, null until a level is reached; {@code reachedLevels} rides beside it in
 * the payload and is not stored yet.
 */
public record BookPublicationRequest(
        @NotBlank String editionSlug,
        @NotBlank String sourceHash,
        @NotBlank String workSlug,
        @NotBlank String title,
        @NotBlank String author,
        String description,
        @NotNull Language originalLanguage,
        @NotNull Language language,
        @NotBlank String editionType,
        String literaryRegister,
        String sourceEditionSlug,
        String translator,
        int publicationYear,
        String coverUrl,
        @NotNull CEFR cefrLevel,
        int wordCount,
        UUID editionId,
        UUID externalJobId,
        String editionNote,
        Integer chapterCount,
        CEFR adaptsTo,
        List<CEFR> reachedLevels) {}
