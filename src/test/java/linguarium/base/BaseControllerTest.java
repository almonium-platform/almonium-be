package linguarium.base;

import com.fasterxml.jackson.databind.ObjectMapper;
import linguarium.auth.common.repository.PrincipalRepository;
import linguarium.config.security.jwt.TokenProvider;
import linguarium.user.core.service.UserService;
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
