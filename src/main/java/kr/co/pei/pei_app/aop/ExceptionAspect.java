package kr.co.pei.pei_app.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * 예외 발생 추적 AOP
 */
@Slf4j
@Aspect
@Component
public class ExceptionAspect {

    @AfterThrowing(pointcut = "execution(* kr.co.pei.pei_app.application.service..*(..))", throwing = "ex")
    public void logException(JoinPoint joinPoint, Exception ex) {
        log.error("[AOP EXCEPTION] {} - 예외 발생: {}", joinPoint.getSignature(), ex.getMessage(), ex);
    }
}
