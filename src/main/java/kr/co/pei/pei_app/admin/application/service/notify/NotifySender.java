package kr.co.pei.pei_app.admin.application.service.notify;

import kr.co.pei.pei_app.domain.entity.notify.Notify;
import kr.co.pei.pei_app.domain.entity.users.Users;
import kr.co.pei.pei_app.domain.repository.notify.EmitterRepository;
import kr.co.pei.pei_app.domain.repository.notify.NotifyRepository;
import kr.co.pei.pei_app.domain.repository.users.UsersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * NotifySender Component 로 분리
 * 현재 컴포넌트를 여러 도메인이 참조 (AOP 와 연동)
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class NotifySender {

    private final NotifyRepository notifyRepository;
    private final UsersRepository usersRepository;
    private final EmitterRepository emitterRepository;

    public void sendNotification(String message, String type, String url, Long targetId) {
        log.info("sendNotification 실행");
        List<Users> allUsers = usersRepository.findAll();

        for (Users users : allUsers) {
            Notify notify = Notify.builder()
                    .receiverId(users.getId())
                    .message(message)
                    .isRead(false)
                    .createdAt(LocalDateTime.now())
                    .type(type)
                    .url(url)
                    .targetId(targetId)
                    .isDisplayed(false)
                    .build();

            notifyRepository.save(notify); // mongoDB

            SseEmitter emitter = emitterRepository.get(users.getId());
            log.info("emitter 에 들어갈 유저 아이디 : {} ", users.getId());
            log.info("emitter 객체 : {}", emitter);
            if (emitter != null) {
                try {
                    emitter.send(SseEmitter.event()
                            .data(message));
                    log.info("에미터 샌더 호출 횟수");
                } catch (IOException e) {
                    emitter.completeWithError(e);
                    emitterRepository.remove(users.getId());
                }
            }
        }
    }
}
