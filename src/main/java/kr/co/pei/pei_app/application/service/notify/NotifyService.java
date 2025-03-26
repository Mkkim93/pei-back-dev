package kr.co.pei.pei_app.application.service.notify;

import kr.co.pei.pei_app.application.dto.notify.NotifyPostDTO;
import kr.co.pei.pei_app.application.service.auth.AuthService;
import kr.co.pei.pei_app.application.service.auth.UsersContextService;
import kr.co.pei.pei_app.domain.entity.notify.Notify;
import kr.co.pei.pei_app.domain.entity.users.Users;
import kr.co.pei.pei_app.domain.repository.notify.NotifyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class NotifyService {

    private final AuthService authService;
    private final UsersContextService usersContextService;
    private final NotifyRepository notifyRepository;
    private final Map<Long, SseEmitter> emitters = new ConcurrentHashMap<>();

    public SseEmitter subscribe() {

        Users users = usersContextService.getCurrentUser();


        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        emitters.put(users.getId(), emitter);

        emitter.onCompletion(() -> emitters.remove(users.getId()));
        emitter.onTimeout(() -> emitters.remove(users.getId()));

        try {
            emitter.send(SseEmitter.event().name("connect").data("connected"));
        } catch (IOException e) {
            emitter.completeWithError(e);
        }
        return emitter;
    }

    public void sendNotification(NotifyPostDTO notifyPostDTO) {
        Notify notify = Notify.builder()
                .receiverId(notifyPostDTO.getReceiverId())
                .message(notifyPostDTO.getMessage())
                .isRead(false)
                .createdAt(LocalDateTime.now())
                .type(notifyPostDTO.getType())
                .url(notifyPostDTO.getUrl())
                .targetId(notifyPostDTO.getTargetId())
                .build();

        notifyRepository.save(notify); // mongoDB

        SseEmitter emitter = emitters.get(notifyPostDTO.getReceiverId());
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event()
                        .name("notification")
                        .data(notifyPostDTO.getMessage()));
            } catch (IOException e) {
                emitter.completeWithError(e);
                emitters.remove(notifyPostDTO.getReceiverId());
            }
        }
    }

    // TODO 읽음 처리 메서드
    public void markAsRead(Long usersId) {

    }

    // TODO 알림 삭제 처리 메서드
}
