package com.almonium.learning.cover.client;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

import com.almonium.analyzer.client.exception.ApiIntegrationException;
import com.almonium.config.properties.CoverProperties;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;
import tools.jackson.databind.json.JsonMapper;

class CoverJudgeTest {
    CoverJudge judge =
            new CoverJudge(mock(RestTemplate.class), JsonMapper.builder().build(), new CoverProperties());

    @Test
    void readsThePickAndWhereTheSubjectSitsEvenWhenTheAnswerIsWrappedInProse() {
        CoverJudge.Verdict verdict = judge.read(
                "Here you go:\n```json\n{\"pick\": 2, \"focus_x\": 40, \"focus_y\": 130, \"reason\": \"The creature.\"}\n```",
                3,
                "m");

        assertThat(verdict.pick()).isEqualTo(1);
        assertThat(verdict.focusX()).isEqualTo(40);
        assertThat(verdict.focusY()).isEqualTo(100);
        assertThat(verdict.reason()).isEqualTo("The creature.");
    }

    @Test
    void aPickOfZeroOrOutsideWhatWasShownIsNoPick() {
        assertThat(judge.read("{\"pick\": 0, \"reason\": \"All title pages.\"}", 3, "m")
                        .pick())
                .isEqualTo(-1);
        assertThat(judge.read("{\"pick\": 9}", 3, "m").pick()).isEqualTo(-1);
    }

    @Test
    void anAnswerThatIsNotAVerdictIsAnError() {
        assertThatThrownBy(() -> judge.read("I cannot help with that.", 3, "m"))
                .isInstanceOf(ApiIntegrationException.class);
    }
}
