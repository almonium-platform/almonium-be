package com.linguatool.configuration.properties;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

//@Data
//@NoArgsConstructor
//@AllArgsConstructor
@Configuration
//@FieldDefaults(level = PRIVATE)
//@ConfigurationProperties(prefix = "gateway")
public class UrbanConfigurationProperties {

    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

    // Second Method: Using RestTemplateBuilder
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

}
