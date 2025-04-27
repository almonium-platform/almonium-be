package com.almonium;

import com.almonium.config.GoogleCloudTestConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import(GoogleCloudTestConfig.class)
public class ApplicationContextLoadTest {
    @Test
    void contextLoads() {}
}
