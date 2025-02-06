package com.almonium.config.integration;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.config.properties.StreamProperties;
import io.getstream.chat.java.services.framework.DefaultClient;
import java.util.Properties;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("!test")
@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class StreamConfig {
    StreamProperties streamProperties;

    @PostConstruct
    public void initializeStreamClient() {
        Properties properties = new Properties();
        properties.put(DefaultClient.API_KEY_PROP_NAME, streamProperties.getKey());
        properties.put(DefaultClient.API_SECRET_PROP_NAME, streamProperties.getSecret());
        DefaultClient.setInstance(new DefaultClient(properties));
    }
}
