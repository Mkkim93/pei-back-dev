package kr.co.pei.pei_app.application.service.smtp;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailAuthService {

    @Value("${spring.mail.username}")
    private String sender;
    private final JavaMailSender javaMailSender;

    public String sendPassword(String temp, String userMail) {

        try {

            String title = "[PEI] 비밀번호 변경 링크입니다."; // TODO
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(sender);
            helper.setTo(userMail);
            helper.setText(temp, true);
            helper.setSubject(title);

            javaMailSender.send(message);
            log.info("임시 비밀번호 전송 완료 수신자 메일: {}", userMail);

            return temp;

        } catch (MessagingException e) { // 예외 전환 : MessagingEx 를 MailSendEx 로 감싸서 던짐
            log.error("메일 전송 실패: {}", e.getMessage());
            throw new MailSendException("메일 전송에 실패하였습니다.", e);
        }
    }
}
