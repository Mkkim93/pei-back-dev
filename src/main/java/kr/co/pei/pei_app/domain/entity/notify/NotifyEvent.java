package kr.co.pei.pei_app.domain.entity.notify;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface NotifyEvent {

    String message() default "";
    String type();
    String url() default "";
    long targetId() default -1L;
}
