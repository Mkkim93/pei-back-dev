package kr.co.pei.pei_app.aop;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AuditLogContext {

    private static ThreadLocal<String> userMail = new ThreadLocal<>();

    public static void setUserMail(String mail) {
        userMail.set(mail);
    }

    public static String getUserMail() {
        return userMail.get();
    }

    public static void clear() {
        userMail.remove();
    }
}
