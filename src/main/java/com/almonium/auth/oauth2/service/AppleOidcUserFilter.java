package com.almonium.auth.oauth2.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class AppleOidcUserFilter implements Filter {
    private final ObjectMapper objectMapper;

    @Autowired
    private AppleUserStore appleUserStore;

    public AppleOidcUserFilter() {
        objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    @SneakyThrows
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) {
        try {
            String jsonUser = request.getParameter("user");
            if (jsonUser != null) {
                log.debug("User parameter is present: first Apple login.");
                AppleUser user = objectMapper.readValue(jsonUser, AppleUser.class);
                appleUserStore.setAppleUser(null); // todo
            }
        } catch (JsonProcessingException e) {
            log.error("JSON parse error of user attribute", e);
        } finally {
            chain.doFilter(request, response);
            appleUserStore.removeAppleUser();
        }
    }

    @Override
    public void destroy() {
    }
}
