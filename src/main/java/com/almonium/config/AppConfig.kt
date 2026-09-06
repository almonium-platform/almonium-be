package com.almonium.config

import com.almonium.config.properties.AlmoProperties
import com.almonium.config.properties.AppProperties
import com.almonium.config.properties.ExternalApiProperties
import com.almonium.config.properties.GoogleProperties
import com.almonium.config.properties.OpenAiProperties
import com.almonium.config.properties.PaddleProperties
import com.almonium.config.properties.RabbitMQProperties
import com.almonium.config.properties.StreamProperties
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
    AlmoProperties::class,
    AppProperties::class,
    ExternalApiProperties::class,
    GoogleProperties::class,
    OpenAiProperties::class,
    PaddleProperties::class,
    RabbitMQProperties::class,
    StreamProperties::class,
)
class AppConfig
