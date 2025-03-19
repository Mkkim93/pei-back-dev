package kr.co.pei.pei_app.domain.entity.log;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/*
    활동 로그를 적용할 커스텀 인터페이스
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface AuditLog {
    String action();
    String description() default "";
}
