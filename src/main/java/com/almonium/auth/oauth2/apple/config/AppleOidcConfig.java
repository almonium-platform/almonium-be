package com.almonium.auth.oauth2.apple.config;

import com.almonium.auth.oauth2.apple.filter.AppleOidcUserFilter;
import com.almonium.config.properties.AppleOAuthProperties;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class AppleOidcConfig {
    private final AppleOAuthProperties appleOAuthProperties;

    @Bean
    public AppleOidcUserFilter appleOidcUserFilter() {
        return new AppleOidcUserFilter();
    }

    @Bean
    public FilterRegistrationBean<AppleOidcUserFilter> appleOidcUserFilterFilterRegistrationBean(
            AppleOidcUserFilter filter) throws URISyntaxException {
        FilterRegistrationBean<AppleOidcUserFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(filter);
        registrationBean.setOrder(-100);
        registrationBean.setUrlPatterns(List.of(new URI(appleOAuthProperties.getRedirectUri()).getPath()));
        return registrationBean;
    }
}
