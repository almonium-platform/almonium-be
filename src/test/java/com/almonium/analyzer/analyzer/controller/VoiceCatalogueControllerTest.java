package com.almonium.analyzer.analyzer.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.almonium.analyzer.translator.model.enums.LanguageVariety;
import com.almonium.analyzer.translator.service.VoiceCatalogue;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

class VoiceCatalogueControllerTest {
    @Test
    void exposesAllRoutesAndFiltersByLanguageWithUnavailableChoicesIntact() throws Exception {
        var mvc = MockMvcBuilders.standaloneSetup(new VoiceCatalogueController(new VoiceCatalogue()))
                .build();
        mvc.perform(get("/lang/voices"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(LanguageVariety.values().length));
        mvc.perform(get("/lang/voices/DE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0].language").value("DE"))
                .andExpect(jsonPath("$[0].variety").value("de-DE"))
                .andExpect(jsonPath("$[0].defaultVariety").value(true))
                .andExpect(jsonPath("$[0].available").value(true))
                .andExpect(jsonPath("$[0].voiceId").value("de-DE-Chirp3-HD-Charon"))
                .andExpect(jsonPath("$[0].gender").value("MALE"))
                .andExpect(jsonPath("$[2].variety").value("de-CH"))
                .andExpect(jsonPath("$[2].available").value(false))
                .andExpect(jsonPath("$[2].unavailableReason").value("NO_ENABLED_VOICE"));
        mvc.perform(get("/lang/voices/LA"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].available").value(false));
        mvc.perform(get("/lang/voices/INVALID")).andExpect(status().isBadRequest());
    }
}
