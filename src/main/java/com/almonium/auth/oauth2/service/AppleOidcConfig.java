package com.almonium.auth.oauth2.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppleOidcConfig {

    @Value("${spring.security.oauth2.client.registration.apple.redirect-pattern:/login/oauth2/code/apple}")
    private String appleRedirectPattern;

    @Bean
    public AppleOidcUserFilter appleOidcUserFilter() {
        return new AppleOidcUserFilter();
    }

    @Bean
    public FilterRegistrationBean<AppleOidcUserFilter> appleOidcUserFilterFilterRegistrationBean(
            AppleOidcUserFilter filter) {
        FilterRegistrationBean<AppleOidcUserFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(filter);
        registrationBean.setOrder(-100);
        registrationBean.setUrlPatterns(List.of(appleRedirectPattern));
        return registrationBean;
    }
}
