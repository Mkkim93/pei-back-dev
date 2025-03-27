package kr.co.pei.pei_app.web.controller.notify;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.pei.pei_app.application.dto.notify.NotifyPostDTO;
import kr.co.pei.pei_app.application.service.notify.NotifyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Tag(name = "NOTIFY_API", description = "모든 도메인의 새로운 이벤트 발생 시 사용자에게 알림을 보내기 위한 API")
@RestController
@RequestMapping("/api/notify")
@RequiredArgsConstructor
public class NotifyController {

    private final NotifyService notifyService;

    @Operation(
            summary = "알림 구독 (SSE 연결)",
            description = """
        클라이언트가 이 엔드포인트로 연결하면 서버와 실시간으로 알림 스트리밍을 시작합니다.  
        이 API는 Server-Sent Events(SSE)를 사용하며, 응답은 계속 유지됩니다.  
        Swagger UI에서는 테스트되지 않으며, 프론트엔드에서는 `EventSource`를 사용해야 합니다.
        """
    )
    @ApiResponse(
            responseCode = "200",
            description = "SSE 연결 성공",
            content = @Content(mediaType = "text/event-stream")
    )
    @GetMapping(value = "/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe(@RequestParam("token") String token) {
        return notifyService.subscribe(token);
    }

    // TODO
    @Operation
    @PostMapping("/send")
    public ResponseEntity<Void> send(@RequestBody NotifyPostDTO notifyPostDTO) {
        notifyService.sendNotification(notifyPostDTO);
        return ResponseEntity.ok().build();
    }
}
