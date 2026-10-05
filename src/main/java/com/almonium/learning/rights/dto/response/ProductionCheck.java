package com.almonium.learning.rights.dto.response;

import com.almonium.learning.rights.model.RightsStatus;
import java.util.List;
import java.util.UUID;

/**
 * Whether a work may be prepared: its status where editions are made and hosted, when it becomes free there, and
 * the catalogue work the verdict is based on (null when only the edition's own facts were available).
 */
public record ProductionCheck(
        RightsStatus status,
        Integer freeFrom,
        List<String> countries,
        UUID catalogWorkId,
        String catalogTitle,
        String catalogAuthor,
        String explanation) {}
