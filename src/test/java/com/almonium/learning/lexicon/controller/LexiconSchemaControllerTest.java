package com.almonium.learning.lexicon.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.almonium.learning.lexicon.controller.open.LexiconSchemaController;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

class LexiconSchemaControllerTest {
    /** What the published schema keeps for the pipeline's own use; published_schema.py names the same. */
    private static final List<String> INTERNAL = List.of("source", "dropped", "retired_lexemes");

    private final ObjectMapper trees = new ObjectMapper();
    private final MockMvc mvc =
            MockMvcBuilders.standaloneSetup(new LexiconSchemaController()).build();

    @Test
    void servesTheSchemaAsAFileToSave() throws Exception {
        byte[] body = mvc.perform(get("/public/lexicon/schema"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "application/json"))
                .andExpect(header().string("Cache-Control", "max-age=3600, public"))
                .andExpect(header().string("Content-Disposition", "attachment; filename=\"lexicon-entry.schema.json\""))
                .andReturn()
                .getResponse()
                .getContentAsByteArray();

        assertThat(trees.readTree(body)).isEqualTo(read(LexiconSchemaController.LOCATION));
    }

    /** Stale after a change to the schema: run scripts/lexicon_research/published_schema.py, which writes both. */
    @Test
    void isThePublishedSchemaWithoutWhatIsThePipelinesOwn() throws IOException {
        ObjectNode published = (ObjectNode) read("lexicon/entry.schema.json");
        ObjectNode offered = (ObjectNode) read(LexiconSchemaController.LOCATION);

        assertThat(offered.path("properties").properties())
                .extracting(java.util.Map.Entry::getKey)
                .doesNotContainAnyElementsOf(INTERNAL);
        ObjectNode lexeme = (ObjectNode) published.path("$defs").path("lexeme").path("properties");
        lexeme.remove("retired");
        assertThat(offered.path("$defs")).isEqualTo(published.path("$defs"));
        ((ObjectNode) published.path("properties")).remove(INTERNAL);
        assertThat(offered.path("properties")).isEqualTo(published.path("properties"));
    }

    /** Nothing in the file says how an entry was made: no source named, no writer, no prompt. */
    @Test
    void saysNothingOfHowAnEntryWasMade() throws IOException {
        String text = read(LexiconSchemaController.LOCATION).toString();

        assertThat(text).doesNotContain("Wiktionary", "evidence", "writer", "prompt", "ingest", "\"from\"");
    }

    private JsonNode read(String location) throws IOException {
        return trees.readTree(new ClassPathResource(location).getInputStream());
    }
}
