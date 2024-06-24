package com.almonium.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Slf4j
@Aspect
@Component
public class LoggingAspect {
    private static final String BASE_PACKAGE = "com.almonium";
    private static final String FILTER_PATH = BASE_PACKAGE + ".auth.common.filter.TokenAuthenticationFilter";
    private static final String POINTCUT = "within(" + BASE_PACKAGE + "..*) && !within(" + FILTER_PATH + ")";

    @Pointcut(POINTCUT)
    public void publicMethod() {
        // Pointcut for public methods in linguarium package and sub-packages, excluding TokenAuthenticationFilter
    }

    @Before("publicMethod()")
    public void logMethodEntry(JoinPoint joinPoint) {
        if (log.isDebugEnabled()) {
            log.debug("Entering: {}", joinPoint.getSignature().toShortString());
            log.trace("With arguments: {}", joinPoint.getArgs());
        }
    }

    @AfterReturning(pointcut = "publicMethod()", returning = "result")
    public void logMethodExit(JoinPoint joinPoint, Object result) {
        if (log.isTraceEnabled()) {
            log.trace(
                    "Exiting method: {} with result: {}",
                    joinPoint.getSignature().toShortString(),
                    result);
        }
    }

    @Around("publicMethod()")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object proceed = joinPoint.proceed();
        long executionTime = System.currentTimeMillis() - start;
        log.debug("{} executed in {} ms", joinPoint.getSignature().toShortString(), executionTime);
        return proceed;
    }

    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void controller() {
        // Pointcut for methods in RestController annotated classes
    }

    @Before("controller()")
    public void logRequest(JoinPoint joinPoint) {
        HttpServletRequest request =
                ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        log.info(
                "Incoming request: {} {} from {}",
                request.getMethod(),
                request.getRequestURI(),
                request.getRemoteAddr());
    }

    @AfterReturning("controller()")
    public void logResponse(JoinPoint joinPoint) {
        HttpServletResponse response =
                ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getResponse();
        if (response != null) {
            log.info("Outgoing response with status: {}", response.getStatus());
        }
    }
}
