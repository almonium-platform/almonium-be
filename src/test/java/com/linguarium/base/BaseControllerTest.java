package com.linguarium.base;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.linguarium.configuration.security.jwt.TokenProvider;
import com.linguarium.user.service.impl.LocalUserDetailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

@ContextConfiguration
public abstract class BaseControllerTest {
    @Autowired
    protected MockMvc mockMvc;

    @MockBean
    protected LocalUserDetailServiceImpl localUserDetailsService;

    @MockBean
    protected TokenProvider tokenProvider;

    @Autowired
    protected ObjectMapper objectMapper;
}
