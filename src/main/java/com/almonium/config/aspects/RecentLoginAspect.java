package com.almonium.config.aspects;

import com.almonium.auth.common.exception.RecentLoginRequiredException;
import com.almonium.auth.common.util.CookieUtil;
import com.almonium.auth.token.service.impl.AuthTokenService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
@RequiredArgsConstructor
public class RecentLoginAspect {
    private final AuthTokenService authTokenService;

    @Around(
            "@annotation(com.almonium.auth.common.annotation.RequireRecentLogin) || within(@com.almonium.auth.common.annotation.RequireRecentLogin *)")
    public Object validateRecentLogin(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes)
                        Objects.requireNonNull(RequestContextHolder.getRequestAttributes()))
                .getRequest();

        String accessToken = CookieUtil.getCookie(request, CookieUtil.ACCESS_TOKEN_COOKIE_NAME)
                .map(Cookie::getValue)
                .orElse(null);

        if (accessToken == null
                || !authTokenService.validateToken(accessToken)
                || !authTokenService.isAccessTokenLive(accessToken)) {
            throw new RecentLoginRequiredException("User must have logged in manually within the last 15 minutes.");
        }
        return joinPoint.proceed();
    }
}
