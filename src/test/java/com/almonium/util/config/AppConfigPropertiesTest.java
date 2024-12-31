package com.almonium.util.config;

import com.almonium.config.properties.AppProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@ContextConfiguration(classes = {TestConfig.class})
@TestPropertySource("classpath:application.yaml")
public abstract class AppConfigPropertiesTest {

    @Autowired
    protected AppProperties appProperties;
}
