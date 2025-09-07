package com.almonium.config;

import com.almonium.config.resolver.AuthIdArgumentResolver;
import com.almonium.config.resolver.AuthUserArgumentResolver;
import com.almonium.user.core.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {
    private final UserRepository userRepository;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new AuthIdArgumentResolver());
        resolvers.add(new AuthUserArgumentResolver(userRepository));
    }
}
