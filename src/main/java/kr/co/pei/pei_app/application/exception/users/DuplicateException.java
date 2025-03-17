package kr.co.pei.pei_app.application.exception.users;

public class DuplicateException extends RuntimeException {
    public DuplicateException(String useranme) {
        super("입력하신 계정은 " + useranme + " 는(은) 이미 존재하는 계정 입니다.");
    }
}
