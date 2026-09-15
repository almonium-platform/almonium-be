package com.almonium;

import com.almonium.config.GoogleCloudTestConfig;
import com.almonium.config.PostgresContainer;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.context.ImportTestcontainers;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import(GoogleCloudTestConfig.class)
@ImportTestcontainers(PostgresContainer.class)
public class ApplicationContextLoadTest {
    @Test
    void contextLoads() {}
}
