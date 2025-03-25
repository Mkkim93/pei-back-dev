package kr.co.pei.pei_app.web.controller.notify;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.pei.pei_app.application.dto.api.ApiResult;
import kr.co.pei.pei_app.application.dto.notify.NotifyPostDTO;
import kr.co.pei.pei_app.application.service.notify.NotifyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Tag(name = "NOTIFY_API", description = "모든 도메인의 새로운 이벤트 발생 시 사용자에게 알림을 보내기 위한 API")
@RestController
@RequestMapping("/api/notify")
@RequiredArgsConstructor
public class NotifyController {

    private final NotifyService notifyService;

    @Operation
    @GetMapping(value = "/subscribe", produces = "text/event-stream")
    public ResponseEntity<ApiResult<SseEmitter>> subscribe() {
        SseEmitter subscribe = notifyService.subscribe();
        return ResponseEntity.status(HttpStatus.OK).body(ApiResult.success("알림 연동 성공", subscribe));
    }

    @Operation
    @PostMapping("/send")
    public ResponseEntity<Void> send(@RequestBody NotifyPostDTO notifyPostDTO) {
        notifyService.sendNotification(notifyPostDTO);
        return ResponseEntity.ok().build();
    }
}
