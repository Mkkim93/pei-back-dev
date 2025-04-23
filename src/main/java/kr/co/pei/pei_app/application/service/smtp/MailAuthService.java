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
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailAuthService {

    @Value("${spring.mail.username}")
    private String sender;
    private final JavaMailSender javaMailSender;

    public void sendPassword(Map<String, Object> savedMap) {

        String userMail = savedMap.get("mail").toString();
        String userUUID = savedMap.get("uuid").toString();

        try {

            String title = "[PEI] 비밀번호 변경 링크입니다."; // TODO
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            String path = "src/main/resources/templates/auth/email/mail.html";
            String resetLink = "http://localhost:3000/reset-password?token=" + userUUID;

            String html = new String(Files.readAllBytes(Paths.get(path)), StandardCharsets.UTF_8);
            html = html.replace("{{RESET_LINK}}", resetLink);

            helper.setFrom(sender);
            helper.setTo(userMail);
            helper.setText(html, true);
            helper.setSubject(title);

            javaMailSender.send(message);
            log.info("임시 비밀번호 전송 완료 수신자 메일: {}", userMail);
            log.info("메일 전송 성공");
        } catch (MessagingException e) { // 예외 전환 : MessagingEx 를 MailSendEx 로 감싸서 던짐
            log.error("메일 전송 실패: {}", e.getMessage());
            throw new MailSendException("메일 전송에 실패하였습니다.", e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
