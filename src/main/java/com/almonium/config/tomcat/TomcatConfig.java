package com.almonium.config.tomcat;

import org.apache.catalina.core.StandardHost;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TomcatConfig {

    @Bean
    public WebServerFactoryCustomizer<TomcatServletWebServerFactory> tomcatCustomizer() {
        return factory -> factory.addContextCustomizers(context -> {
            try {
                StandardHost host = (StandardHost) context.getParent();
                host.setErrorReportValveClass(CustomTomcatErrorValve.class.getName());
            } catch (Exception e) {
                throw new RuntimeException("Failed to set custom ErrorReportValve", e);
            }
        });
    }
}
