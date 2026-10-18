package com.almonium.learning.lexicon.mapper;

import com.almonium.learning.lexicon.dto.response.ClipSubmissionResult;
import com.almonium.learning.lexicon.dto.response.UnvoicedPronunciation;
import com.almonium.learning.lexicon.model.entity.AudioClip;
import com.almonium.learning.lexicon.model.entity.Lexeme;
import com.almonium.learning.lexicon.model.entity.Pronunciation;
import org.springframework.stereotype.Component;

/** Shapes pronunciations and clips for the audio phase's two internal routes. */
@Component
public class AudioClipMapper {

    public UnvoicedPronunciation toUnvoiced(Pronunciation pronunciation) {
        Lexeme lexeme = pronunciation.getLexeme();
        return new UnvoicedPronunciation(
                pronunciation.getId(),
                lexeme.getId(),
                lexeme.getHeadword(),
                lexeme.getLemma(),
                lexeme.getPos(),
                pronunciation.getForm() == null ? null : pronunciation.getForm().getId(),
                pronunciation.getForm() == null ? null : pronunciation.getForm().getForm(),
                pronunciation.getVariety(),
                pronunciation.getRank(),
                pronunciation.getPhonemic(),
                pronunciation.getReading(),
                pronunciation.getEngineInput());
    }

    public ClipSubmissionResult toResult(AudioClip clip, boolean stored) {
        return new ClipSubmissionResult(clip.getId(), clip.getObjectKey(), stored);
    }
}
