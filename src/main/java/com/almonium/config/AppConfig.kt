package com.almonium.config

import com.almonium.config.properties.AiProperties
import com.almonium.config.properties.AppProperties
import com.almonium.config.properties.AppleOAuthProperties
import com.almonium.config.properties.AppleOAuthProviderProperties
import com.almonium.config.properties.ExternalApiProperties
import com.almonium.config.properties.GoogleProperties
import com.almonium.config.properties.MailProperties
import com.almonium.config.properties.RabbitMQProperties
import com.almonium.config.properties.StreamProperties
import com.almonium.config.properties.StripeProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.EnableAspectJAutoProxy
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.scheduling.annotation.EnableScheduling

@Configuration
@EnableJpaAuditing
@EnableAspectJAutoProxy
@EnableScheduling
@EnableConfigurationProperties(
    AiProperties::class,
    AppProperties::class,
    AppleOAuthProperties::class,
    AppleOAuthProviderProperties::class,
    ExternalApiProperties::class,
    GoogleProperties::class,
    MailProperties::class,
    RabbitMQProperties::class,
    StripeProperties::class,
    StreamProperties::class,
)
class AppConfig
