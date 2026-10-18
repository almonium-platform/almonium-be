package com.almonium.learning.lexicon.audio;

import java.util.Optional;

/**
 * Where pronunciation clips are kept. Clips are immutable and content-addressed: a key names one rendering for good,
 * so a write is {@link #putIfAbsent} and there is no delete, on purpose. The backend alone holds the credentials;
 * readers fetch clips through it.
 */
public interface AudioStore {
    boolean exists(String key);

    /** Writes the bytes under the key unless something is already there, in which case it writes nothing. */
    void putIfAbsent(String key, byte[] bytes, String contentType);

    Optional<StoredAudio> get(String key);
}
