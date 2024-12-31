package com.almonium.config;

import com.almonium.config.properties.AppProperties;
import com.almonium.config.properties.AppleOAuthProperties;
import com.almonium.config.properties.ExternalApiProperties;
import com.almonium.config.properties.GoogleProperties;
import com.almonium.config.properties.MailProperties;
import com.almonium.config.properties.OpenAIProperties;
import com.almonium.config.properties.StripeProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * The AppConfig class is a configuration class to add the necessary annotations to the application.
 */
@Configuration
@EnableJpaAuditing
@EnableAspectJAutoProxy
@EnableConfigurationProperties({
    AppProperties.class,
    AppleOAuthProperties.class,
    ExternalApiProperties.class,
    GoogleProperties.class,
    MailProperties.class,
    OpenAIProperties.class,
    StripeProperties.class,
})
public class AppConfig {}
