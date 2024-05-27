package com.linguarium.base;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.linguarium.config.security.jwt.TokenProvider;
import com.linguarium.user.core.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@ContextConfiguration
public abstract class BaseControllerTest {
    @Autowired
    protected MockMvc mockMvc;

    @MockBean
    protected UserService userService;

    @MockBean
    protected TokenProvider tokenProvider;

    @Autowired
    protected ObjectMapper objectMapper;
}
