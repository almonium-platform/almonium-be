package com.almonium.learning.catalog.dto.response;

import com.almonium.learning.rights.model.RightsStatus;

/**
 * One library's text of a work as a reader may choose it (W1): where it comes from, in which language, whether it
 * is a translation, whether the book processor has checked its shape, and whether it is the default the want is
 * made from. The download address is the stable key a want names its choice by; the source ids change with every
 * catalogue import.
 */
public record SourceOption(
        String kind,
        String language,
        String label,
        boolean translation,
        String pageUrl,
        String downloadUrl,
        RightsStatus status,
        String shape,
        boolean preferred) {}
