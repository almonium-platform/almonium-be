package com.almonium.learning.lexicon.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.almonium.learning.lexicon.controller.open.LexiconClipController;
import com.almonium.learning.lexicon.dto.response.ClipContent;
import com.almonium.learning.lexicon.service.AudioClipService;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class LexiconClipControllerTest {
    private static final byte[] CLIP = {(byte) 0xFF, (byte) 0xFB, (byte) 0x90, 0x64, 1, 2, 3};

    @Mock
    AudioClipService clipService;

    MockMvc mvc;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders.standaloneSetup(new LexiconClipController(clipService))
                .build();
    }

    @Test
    void streamsAClipAsImmutableMpegAudioWithItsHashAsETag() throws Exception {
        UUID clipId = UUID.randomUUID();
        when(clipService.read(clipId)).thenReturn(Optional.of(new ClipContent(CLIP, "0123abcd")));

        mvc.perform(get("/public/lexicon/clips/{id}", clipId))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "audio/mpeg"))
                .andExpect(header().longValue("Content-Length", CLIP.length))
                .andExpect(header().string("Cache-Control", "public, max-age=31536000, immutable"))
                .andExpect(header().string("ETag", "\"0123abcd\""))
                .andExpect(content().bytes(CLIP));
    }

    @Test
    void answers404WhenTheClipOrItsObjectIsMissing() throws Exception {
        UUID clipId = UUID.randomUUID();
        when(clipService.read(clipId)).thenReturn(Optional.empty());

        mvc.perform(get("/public/lexicon/clips/{id}", clipId)).andExpect(status().isNotFound());
        mvc.perform(get("/public/lexicon/clips/not-a-uuid")).andExpect(status().isBadRequest());
    }
}
