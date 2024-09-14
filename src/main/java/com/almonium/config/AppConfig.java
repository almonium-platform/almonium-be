package com.almonium.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * The AppConfig class is a configuration class to add the necessary annotations to the application.
 */
@Configuration
@EnableJpaAuditing
@EnableAspectJAutoProxy
public class AppConfig {}
