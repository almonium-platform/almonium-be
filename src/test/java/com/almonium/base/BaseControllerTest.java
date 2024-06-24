package com.almonium.base;

import com.almonium.auth.common.repository.PrincipalRepository;
import com.almonium.auth.common.service.impl.TokenProvider;
import com.almonium.user.core.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    @MockBean
    protected PrincipalRepository principalRepository;

    @Autowired
    protected ObjectMapper objectMapper;
}
