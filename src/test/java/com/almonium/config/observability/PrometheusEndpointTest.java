package com.almonium.config.observability;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.almonium.config.GoogleCloudTestConfig;
import com.almonium.config.PostgresContainer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.micrometer.metrics.test.autoconfigure.AutoConfigureMetrics;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.context.ImportTestcontainers;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureMetrics
@Import(GoogleCloudTestConfig.class)
@ImportTestcontainers(PostgresContainer.class)
class PrometheusEndpointTest {
    @Autowired
    MockMvc mockMvc;

    @Test
    void scraperReadsMetricsWithoutSigningIn() throws Exception {
        // One request first, so the HTTP latency series exists.
        mockMvc.perform(get("/actuator/info")).andExpect(status().isOk());

        mockMvc.perform(get("/actuator/prometheus"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("application=\"almonium-be\"")))
                .andExpect(content().string(containsString("jvm_memory_used_bytes")))
                .andExpect(content().string(containsString("http_server_requests_seconds_bucket")));
    }

    @Test
    void otherActuatorEndpointsStayClosed() throws Exception {
        mockMvc.perform(get("/actuator/env")).andExpect(status().isUnauthorized());
    }
}
