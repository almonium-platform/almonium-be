package com.almonium.config.aspect;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.auth.common.exception.RecentLoginRequiredException;
import com.almonium.auth.common.util.CookieUtil;
import com.almonium.auth.token.service.AuthTokenService;
import com.almonium.config.properties.AppProperties;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class RecentLoginAspect {
    AuthTokenService authTokenService;
    AppProperties appProperties;

    @Around(
            """
            @annotation(com.almonium.auth.common.annotation.RequireRecentLogin)
            || within(@com.almonium.auth.common.annotation.RequireRecentLogin *)
            """)
    public Object validateRecentLogin(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes)
                        Objects.requireNonNull(RequestContextHolder.getRequestAttributes()))
                .getRequest();

        String accessToken = CookieUtil.getCookie(request, CookieUtil.ACCESS_TOKEN_COOKIE_NAME)
                .map(Cookie::getValue)
                .orElse(null);

        if (accessToken == null
                || !authTokenService.validateToken(accessToken)
                || authTokenService.isAccessTokenRefreshed(accessToken)) {
            throw new RecentLoginRequiredException(String.format(
                    "User must have logged in manually within the last %d minutes.",
                    appProperties.getAuth().getJwt().getAccessToken().getLifetime() / 60));
        }
        return joinPoint.proceed();
    }
}
