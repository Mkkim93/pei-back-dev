package kr.co.pei.pei_app.aop;

import kr.co.pei.pei_app.admin.application.service.notify.NotifySender;
import kr.co.pei.pei_app.domain.entity.notify.NotifyEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Slf4j
@Aspect
@Component
@Order(1) // 트랜잭션보다 먼저
@RequiredArgsConstructor
public class NotifyAspect {

    private final NotifySender sender;

    @AfterReturning(
            value = "@annotation(notifyEvent)",
            returning = "result"
    )
    public void sendNotify(JoinPoint joinPoint, NotifyEvent notifyEvent, Object result) {
        Object[] args = joinPoint.getArgs();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Long savedBoardId = (Long) result; // TODO

        NotifyEvent notifyEventLog = method.getAnnotation(NotifyEvent.class);

        log.info("notifyEventData.message: {}", notifyEventLog.message());
        args.getClass().getSimpleName().endsWith("DTO");
        log.info("args Name: {}", args.getClass().getName());
        log.info("sendNotify 실행 여부");

        sender.sendNotification(
                notifyEvent.message(),
                notifyEvent.type(),
                notifyEvent.url(),
                savedBoardId
//                notifyEvent.targetId() == -1L ? null : notifyEvent.targetId()
        );
    }
}
