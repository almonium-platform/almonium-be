package com.almonium.auth.oauth2.apple.config;

import com.almonium.auth.oauth2.apple.filter.AppleOidcUserFilter;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppleOidcConfig {

    @Value("${spring.security.oauth2.client.registration.apple.redirect-uri}")
    private String appleRedirectUri;

    @Bean
    public AppleOidcUserFilter appleOidcUserFilter() {
        return new AppleOidcUserFilter(); // todo make it component
    }

    @Bean
    public FilterRegistrationBean<AppleOidcUserFilter> appleOidcUserFilterFilterRegistrationBean(
            AppleOidcUserFilter filter) throws URISyntaxException {
        FilterRegistrationBean<AppleOidcUserFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(filter);
        registrationBean.setOrder(-100);
        registrationBean.setUrlPatterns(List.of(new URI(appleRedirectUri).getPath()));
        return registrationBean;
    }
}
