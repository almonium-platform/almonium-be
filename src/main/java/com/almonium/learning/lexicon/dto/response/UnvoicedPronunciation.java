package com.almonium.learning.lexicon.dto.response;

import java.util.UUID;

/**
 * A pronunciation no clip has been made for, with everything the audio phase needs to synthesise it: the word and
 * its form, the variety to say it in, the IPA, and any reading or engine input an earlier pass left behind.
 */
public record UnvoicedPronunciation(
        UUID id,
        UUID lexemeId,
        String headword,
        String lemma,
        String pos,
        UUID formId,
        String form,
        String variety,
        int rank,
        String phonemic,
        String reading,
        String engineInput) {}
