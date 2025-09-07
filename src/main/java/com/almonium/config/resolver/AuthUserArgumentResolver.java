package com.almonium.config.resolver;

import com.almonium.auth.common.annotation.Auth;
import com.almonium.user.core.exception.NoPrincipalFoundException;
import com.almonium.user.core.model.entity.User;
import com.almonium.user.core.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public record AuthUserArgumentResolver(UserRepository userRepository) implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterAnnotation(Auth.class) != null
                && parameter.getParameterType().equals(User.class);
    }

    @Override
    public Object resolveArgument(
            @NotNull MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            @NotNull NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory) {

        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) throw new NoPrincipalFoundException("No authentication.");

        var principal = auth.getPrincipal();
        UUID userId;

        if (principal instanceof com.almonium.auth.common.security.SecurityPrincipal sp) {
            userId = sp.userId();
        } else if (principal instanceof com.almonium.auth.common.model.entity.Principal jpa && jpa.getUser() != null) {
            userId = jpa.getUser().getId();
        } else {
            throw new NoPrincipalFoundException("Authenticated principal not found.");
        }

        return userRepository
                .findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + userId));
    }
}
