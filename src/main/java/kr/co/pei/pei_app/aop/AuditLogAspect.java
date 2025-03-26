package kr.co.pei.pei_app.aop;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import kr.co.pei.pei_app.application.service.auth.UsersContextService;
import kr.co.pei.pei_app.domain.entity.log.AuditLog;
import kr.co.pei.pei_app.domain.entity.log.Log;
import kr.co.pei.pei_app.domain.entity.users.Users;
import kr.co.pei.pei_app.domain.repository.log.LogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Method;

/**
 * 활동 로그 AOP
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class AuditLogAspect {

    private final UsersContextService usersContextService;
    private final LogRepository logRepository;
    private final HttpServletRequest request;

    @Around("@annotation(kr.co.pei.pei_app.domain.entity.log.AuditLog)")
    @Transactional
    public Object logActivity(ProceedingJoinPoint joinPoint) throws Throwable {

        Object[] args = joinPoint.getArgs();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        AuditLog auditLog = method.getAnnotation(AuditLog.class);

        Users users = usersContextService.getCurrentUser();

        if (users == null) {
            log.warn("관리자 정보 확인에 실패하였습니다.");
            return joinPoint.proceed();
        }

        ObjectMapper mapper = new ObjectMapper();
        String jsonData = "";

        for (Object arg : args) {
            try {
                // DTO 객체이거나 단순히 JSON 직렬화 가능한 경우 모두 기록
                if (arg != null && (
                        arg.getClass().getSimpleName().endsWith("DTO")
                                || arg instanceof java.util.Collection
                                || arg instanceof java.util.Map
                                || arg.getClass().isArray()
                                || arg instanceof Number
                                || arg instanceof String
                )) {
                    jsonData = mapper.writeValueAsString(arg);
                }
            } catch (Exception e) {
                log.error("JSON 변환 오류", e);
            }
        }

        Object proceed = joinPoint.proceed();

        Log logEntity = Log.builder()
                .users(users)
                .action(auditLog.action())
                .description(jsonData)
                .ipAddress(request.getRemoteAddr())
                .isDeleted(false)
                .userAgent(request.getHeader("User-Agent"))
                .build();

        logRepository.save(logEntity);

        log.info("관리자 활동 로그 저장: {} - {}", auditLog.action(), jsonData);

        return proceed;
    }

}
