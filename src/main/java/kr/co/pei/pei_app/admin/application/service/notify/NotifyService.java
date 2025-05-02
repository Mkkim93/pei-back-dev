package kr.co.pei.pei_app.admin.application.service.notify;

import jakarta.persistence.EntityNotFoundException;
import kr.co.pei.pei_app.admin.application.dto.notify.AdminNotifyFindDTO;
import kr.co.pei.pei_app.domain.entity.notify.Notify;
import kr.co.pei.pei_app.domain.entity.users.Users;
import kr.co.pei.pei_app.domain.repository.notify.EmitterRepository;
import kr.co.pei.pei_app.domain.repository.notify.NotifyRepository;
import kr.co.pei.pei_app.domain.repository.users.UsersRepository;
import kr.co.pei.pei_app.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotifyService {

    private final UsersRepository usersRepository;
    private final JwtUtil jwtUtil;
    private final NotifyRepository notifyRepository;
    private final EmitterRepository emitterRepository;

    public SseEmitter subscribe(String token) {

        String username = jwtUtil.getUsername(token);

        Users usersEntity = usersRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("유저 정보 없음"));

        SseEmitter emitter = new SseEmitter(300_000L); // TODO .env 로 분리
        emitterRepository.save(usersEntity.getId(), emitter);

        emitter.onCompletion(() -> emitterRepository.remove(usersEntity.getId()));
        emitter.onTimeout(() -> emitterRepository.remove(usersEntity.getId()));

        try {
            emitter.send(SseEmitter.event()
                    .name("connect")
                    .data("connected"));
        } catch (IOException e) {
            emitter.completeWithError(e);
        }
        log.info("에미터 실행 횟수");
        return emitter;
    }

    // 모든 알림 조회 (Profile.vue)
    public Page<AdminNotifyFindDTO> findAll(Pageable pageable, Boolean filterIsRead) {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        Users users = usersRepository.findByUsername(username).orElseThrow(() -> new EntityNotFoundException("알림 관련 사용자 조회 오류"));

        Page<Notify> notifyPage;

        if (!filterIsRead) {
            notifyPage = notifyRepository.findByReceiverId(users.getId(), pageable);
        }

        else {
            notifyPage = notifyRepository.findByReceiverIdAndIsReadFalse(users.getId(), pageable);
        }

        List<AdminNotifyFindDTO> content = notifyPage.map(notify -> new AdminNotifyFindDTO(
                notify.getId(),
                notify.getMessage(),
                notify.getType(),
                notify.getCreatedAt(),
                notify.getTargetId(),
                notify.getUrl(),
                notify.getIsRead(),
                notify.getIsDisplayed()
        )).toList();

        return new PageImpl<>(content, pageable, notifyPage.getTotalElements());


    }

    // 전체 알림 중 새로운 알림만 조회
    public Page<AdminNotifyFindDTO> findAllByIsDisplayedFalse(Pageable pageable) {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        Users users = usersRepository.findByUsername(username).orElseThrow(() -> new EntityNotFoundException("알림 관련 사용자 조회 오류"));

        Page<Notify> notifyPage = notifyRepository.findByReceiverIdAndIsDisplayedFalse(users.getId(), pageable);

        // 필터링 + DTO 변환
        List<AdminNotifyFindDTO> content = notifyPage.stream()
                .filter(notify -> !notify.getIsRead())
                .map(notify -> new AdminNotifyFindDTO(
                        notify.getId(),
                        notify.getMessage(),
                        notify.getType(),
                        notify.getCreatedAt(),
                        notify.getTargetId(),
                        notify.getUrl(),
                        notify.getIsRead(),
                        notify.getIsDisplayed()
                )).toList();
        return new PageImpl<>(content, pageable, notifyPage.getTotalElements());
    }

    public void markAsRead(List<String> ids) {
        notifyRepository.markAsRead(ids);
    }

    public void updatedDisplayedTrue(List<String> notifyIds) {
        notifyRepository.markAsDisplayed(notifyIds);
    }
}
