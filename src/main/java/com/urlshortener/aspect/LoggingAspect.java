package com.urlshortener.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class LoggingAspect {
    @Before("@annotation(com.urlshortener.aspect.Loggable)")
    public void logBefore(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().toShortString();
        Object[] args = joinPoint.getArgs();
        log.info("[ENTER] {} with args {}", methodName, args);
    }

    @AfterReturning(pointcut = "@annotation(com.urlshortener.aspect.Loggable)", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        String methodName = joinPoint.getSignature().toShortString();
        log.info("[EXIT] {} returned: {}", methodName, result);
    }

    @AfterThrowing(pointcut = "@annotation(com.urlshortener.aspect.Loggable)", throwing = "ex")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable ex) {
        String methodName = joinPoint.getSignature().toShortString();
        log.error("[ERROR] {} threw exception: {}", methodName, ex.getMessage());
    }
}
