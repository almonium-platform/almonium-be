package com.almonium.config.resolver;

import com.almonium.auth.common.annotation.Auth;
import com.almonium.auth.common.model.entity.Principal;
import com.almonium.user.core.exception.NoPrincipalFoundException;
import lombok.NonNull;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class AuthIdArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterAnnotation(Auth.class) != null
                && parameter.getParameterType().equals(Long.class);
    }

    @Override
    public Object resolveArgument(
            @NonNull MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            @NonNull NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof Principal principal) {
            if (principal.getUser() != null) {
                return principal.getUser().getId();
            }
            throw new IllegalStateException("Authenticated principal does not have an associated user.");
        }
        throw new NoPrincipalFoundException("Authenticated principal not found.");
    }
}
