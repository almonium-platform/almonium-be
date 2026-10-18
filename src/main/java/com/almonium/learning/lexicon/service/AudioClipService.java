package com.almonium.learning.lexicon.service;

import com.almonium.learning.lexicon.audio.AudioStore;
import com.almonium.learning.lexicon.dto.request.ClipSubmission;
import com.almonium.learning.lexicon.dto.response.ClipContent;
import com.almonium.learning.lexicon.dto.response.ClipSubmissionResult;
import com.almonium.learning.lexicon.dto.response.UnvoicedPronunciation;
import com.almonium.learning.lexicon.mapper.AudioClipMapper;
import com.almonium.learning.lexicon.model.ClipObjectKey;
import com.almonium.learning.lexicon.model.entity.AudioClip;
import com.almonium.learning.lexicon.model.entity.Pronunciation;
import com.almonium.learning.lexicon.model.enums.ClipStatus;
import com.almonium.learning.lexicon.repository.AudioClipRepository;
import com.almonium.learning.lexicon.repository.PronunciationRepository;
import jakarta.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Takes the audio phase's clips in and hands them back out. A clip's bytes go to the store under a key derived from
 * the rendering, written once and never replaced; the row for that rendering is created or updated, keeping its id,
 * so a rendering submitted twice is one clip with one URL. A bad submission is an {@link IllegalArgumentException}
 * with the reason, an unknown pronunciation an {@link EntityNotFoundException}; the controller turns them into 400
 * and 404.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class AudioClipService {
    private static final int PACE_SCALE = 2;

    private final PronunciationRepository pronunciationRepository;
    private final AudioClipRepository clipRepository;
    private final AudioStore audioStore;
    private final AudioClipMapper mapper;

    @Transactional(readOnly = true)
    public List<UnvoicedPronunciation> withoutClips(String language, int limit, int offset) {
        return pronunciationRepository.findWithoutClips(language, limit, offset).stream()
                .map(mapper::toUnvoiced)
                .toList();
    }

    public ClipSubmissionResult store(UUID pronunciationId, ClipSubmission submission) {
        Pronunciation pronunciation = pronunciationRepository
                .findById(pronunciationId)
                .orElseThrow(() -> new EntityNotFoundException("No pronunciation " + pronunciationId));
        String engine = required(submission.engine(), "engine");
        String voice = required(submission.voice(), "voice");
        String model = required(submission.model(), "model");
        String format = required(submission.format(), "format");
        String engineInput = required(submission.engineInput(), "engine_input");
        ClipStatus status = ClipStatus.fromLabel(required(submission.status(), "status"));
        if (submission.pace() == null) {
            throw new IllegalArgumentException("pace is required");
        }
        if (submission.durationMs() == null) {
            throw new IllegalArgumentException("duration_ms is required");
        }
        BigDecimal pace = submission.pace().setScale(PACE_SCALE, RoundingMode.HALF_UP);
        byte[] audio = decode(required(submission.audioBase64(), "audio_base64"));
        ClipObjectKey key =
                ClipObjectKey.of(pronunciation.getVariety(), engine, voice, model, pace, format, engineInput);

        boolean stored = !audioStore.exists(key.value());
        if (stored) {
            audioStore.putIfAbsent(key.value(), audio, key.contentType());
        }

        AudioClip clip = clipRepository
                .findByPronunciationIdAndEngineAndVoiceAndModelAndPaceAndFormat(
                        pronunciation.getId(), engine, voice, model, pace, format)
                .orElseGet(() -> {
                    AudioClip fresh = new AudioClip();
                    fresh.setPronunciation(pronunciation);
                    fresh.setEngine(engine);
                    fresh.setVoice(voice);
                    fresh.setModel(model);
                    fresh.setPace(pace);
                    fresh.setFormat(format);
                    fresh.setCreatedAt(Instant.now());
                    return fresh;
                });
        clip.setObjectKey(key.value());
        clip.setByteSize(audio.length);
        clip.setDurationMs(submission.durationMs());
        clip.setStatus(status);
        clip.setTranscript(submission.transcript());
        clip.setCharacterCost(submission.characterCost());
        clipRepository.save(clip);
        pronunciation.setEngineInput(engineInput);
        return mapper.toResult(clip, stored);
    }

    @Transactional(readOnly = true)
    public Optional<ClipContent> read(UUID clipId) {
        return clipRepository
                .findById(clipId)
                .flatMap(clip -> audioStore
                        .get(clip.getObjectKey())
                        .map(audio -> new ClipContent(audio.bytes(), ClipObjectKey.hashOf(clip.getObjectKey()))));
    }

    private static String required(String value, String name) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(name + " is required");
        }
        return value;
    }

    private static byte[] decode(String base64) {
        byte[] audio;
        try {
            audio = Base64.getDecoder().decode(base64);
        } catch (IllegalArgumentException exception) {
            throw new IllegalArgumentException("audio_base64 is not valid base64", exception);
        }
        if (audio.length == 0) {
            throw new IllegalArgumentException("audio_base64 is empty");
        }
        return audio;
    }
}
