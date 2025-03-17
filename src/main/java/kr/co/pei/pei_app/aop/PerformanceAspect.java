package kr.co.pei.pei_app.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class PerformanceAspect {

    @Around("execution(* kr.co.pei.pei_app.application.service..*(..))")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long end = System.currentTimeMillis() - start;
        log.info("[AOP PERFORMANCE] {} \"실행 시간: {}\",", joinPoint.getSignature(), end + "ms");
        return result;
    }
}
