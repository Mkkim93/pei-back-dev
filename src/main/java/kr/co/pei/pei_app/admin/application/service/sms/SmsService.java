package kr.co.pei.pei_app.admin.application.service.sms;

import lombok.extern.slf4j.Slf4j;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SmsService {

    private final DefaultMessageService messageService;
    private final String sender;

    public SmsService(@Value("${coolsms.api.secret}") String secretKey,
                      @Value("${coolsms.api.key}") String apiKey,
                      @Value("${coolsms.api.sender}") String sender) {
        this.messageService = NurigoApp.INSTANCE.initialize(apiKey, secretKey, "https://api.coolsms.co.kr");
        this.sender = sender;
    }

    public void sendSms(String phone, String secretCode) {
        Message message = new Message();
        message.setFrom(sender);
        message.setTo(phone);
        message.setText("[PEI] 인증번호 [" + secretCode + "] (3분 이내에 입력해주세요.)");
        messageService.sendOne(new SingleMessageSendingRequest(message));
    }
}
