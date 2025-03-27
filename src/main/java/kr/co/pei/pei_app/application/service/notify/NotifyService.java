package kr.co.pei.pei_app.application.service.notify;

import jakarta.persistence.EntityNotFoundException;
import kr.co.pei.pei_app.application.dto.notify.NotifyPostDTO;
import kr.co.pei.pei_app.application.service.auth.AuthService;
import kr.co.pei.pei_app.application.service.auth.UsersContextService;
import kr.co.pei.pei_app.domain.entity.notify.Notify;
import kr.co.pei.pei_app.domain.entity.users.Users;
import kr.co.pei.pei_app.domain.repository.notify.NotifyRepository;
import kr.co.pei.pei_app.domain.repository.users.UsersRepository;
import kr.co.pei.pei_app.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotifyService {

    private final UsersRepository usersRepository;
    private final JwtUtil jwtUtil;
    private final NotifyRepository notifyRepository;
    private final Map<Long, SseEmitter> emitters = new ConcurrentHashMap<>();

    public SseEmitter subscribe(String token) {

        String username = jwtUtil.getUsername(token);

        Users usersEntity = usersRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("유저 정보 없음"));

        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        emitters.put(usersEntity.getId(), emitter);

        emitter.onCompletion(() -> emitters.remove(usersEntity.getId()));
        emitter.onTimeout(() -> emitters.remove(usersEntity.getId()));

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
