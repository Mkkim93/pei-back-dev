package kr.co.pei.pei_app.admin.web.controller.notify;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.pei.pei_app.admin.application.dto.api.ApiResult;
import kr.co.pei.pei_app.admin.application.dto.notify.AdminNotifyFindDTO;
import kr.co.pei.pei_app.admin.application.service.notify.NotifyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

import static kr.co.pei.pei_app.admin.application.dto.api.ApiResult.success;
import static org.springframework.data.domain.Sort.Direction.DESC;

@Slf4j
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

    @Operation(
            summary = "내정보에서 모든 알림 목록 조회",
            description = "조건 없이 모든 알림 조회"
    )
    @ApiResponse(responseCode = "200", description = "전체 알림 목록 조회",
    content = @Content(schema = @Schema(implementation = ApiResult.class),
    mediaType = "application/json", examples = @ExampleObject(
            name = "모든 알림 목록 조회 예시",
            summary = "모든 알림 목록 조회 응답 예제",
            value = """
                    {
                      "status": 200,
                      "message": "모든 알림 조회",
                      "timestamp": "2025-04-09T13:08:35.022259",
                      "data": {
                        "content": [
                          {
                            "id": "67f37a09e3f02b695ad374ae",
                            "message": "새 글이 등록되었습니다",
                            "type": "게시글",
                            "createAt": "2025-04-07T16:08:57",
                            "targetId": null,
                            "url": "/api/board",
                            "isRead": false,
                            "isDisplayed": true
                          },
                          {
                            "id": "67f37801e3f02b695ad374a0",
                            "message": "새 글이 등록되었습니다",
                            "type": "게시글",
                            "createAt": "2025-04-07T16:00:17",
                            "targetId": null,
                            "url": "/api/board",
                            "isRead": false,
                            "isDisplayed": true
                          },
                          {
                            "id": "67f377fbe3f02b695ad37499",
                            "message": "새 글이 등록되었습니다",
                            "type": "게시글",
                            "createAt": "2025-04-07T16:00:11",
                            "targetId": null,
                            "url": "/api/board",
                            "isRead": false,
                            "isDisplayed": true
                          },
                          {
                            "id": "67f37a08e3f02b695ad374a7",
                            "message": "새 글이 등록되었습니다",
                            "type": "게시글",
                            "createAt": "2025-04-07T16:08:56",
                            "targetId": null,
                            "url": "/api/board",
                            "isRead": false,
                            "isDisplayed": true
                          },
                          {
                            "id": "67f37a0ce3f02b695ad374c3",
                            "message": "새 글이 등록되었습니다",
                            "type": "게시글",
                            "createAt": "2025-04-07T16:09:00",
                            "targetId": null,
                            "url": "/api/board",
                            "isRead": false,
                            "isDisplayed": false
                          }
                        ],
                        "page": {
                          "size": 5,
                          "number": 1,
                          "totalElements": 32,
                          "totalPages": 7
                        }
                      }
                      }
                    """
    )))
    @GetMapping
    public ResponseEntity<ApiResult<Page<AdminNotifyFindDTO>>> findAll(@ParameterObject @PageableDefault(
            sort = "createdAt", direction = DESC, page = 0, size = 5) Pageable pageable, @RequestParam(value = "filterIsRead", required = false) Boolean isRead) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("알림 조회 시 유저 객체 확인: {}", username);
        Page<AdminNotifyFindDTO> all = notifyService.findAll(pageable, isRead);
        return ResponseEntity.status(HttpStatus.OK).body(success("모든 알림 조회", all));
    }

    @Operation(
            summary = "전체 알림 중 첫 알림 조회",
            description = "한번 랜더링된 알림은 뜨지 않음")
    @ApiResponse(responseCode = "200", description = "알림 조회",
    content = @Content(schema = @Schema(implementation = ApiResult.class),
    mediaType = "application/json", examples = @ExampleObject(
            name = "최초 알림 조회 예시",
            summary = "최초 알림 조회 응답 예제",
            value = """
                    {
                      "status": 200,
                      "message": "전체 알림 조회",
                      "timestamp": "2025-04-08T13:10:13.496794",
                      "data": {
                        "content": [
                          {
                            "id": "67f37a0de3f02b695ad374d1",
                            "message": "새 글이 등록되었습니다",
                            "type": "게시글",
                            "createAt": "2025-04-07T16:09:01",
                            "targetId": null,
                            "url": "/api/board",
                            "isRead": false,
                            "isDisplayed": false
                          },
                          {
                            "id": "67f37a0de3f02b695ad374d8",
                            "message": "새 글이 등록되었습니다",
                            "type": "게시글",
                            "createAt": "2025-04-07T16:09:01",
                            "targetId": null,
                            "url": "/api/board",
                            "isRead": false,
                            "isDisplayed": false
                          },
                          {
                            "id": "67f37a0ce3f02b695ad374c3",
                            "message": "새 글이 등록되었습니다",
                            "type": "게시글",
                            "createAt": "2025-04-07T16:09:00",
                            "targetId": null,
                            "url": "/api/board",
                            "isRead": false,
                            "isDisplayed": false
                          },
                          {
                            "id": "67f37a0ce3f02b695ad374bc",
                            "message": "새 글이 등록되었습니다",
                            "type": "게시글",
                            "createAt": "2025-04-07T16:09:00",
                            "targetId": null,
                            "url": "/api/board",
                            "isRead": false,
                            "isDisplayed": false
                          },
                          {
                            "id": "67f37a0ce3f02b695ad374ca",
                            "message": "새 글이 등록되었습니다",
                            "type": "게시글",
                            "createAt": "2025-04-07T16:09:00",
                            "targetId": null,
                            "url": "/api/board",
                            "isRead": false,
                            "isDisplayed": false
                          }
                        ],
                        "page": {
                          "size": 5,
                          "number": 0,
                          "totalElements": 23,
                          "totalPages": 5
                        }
                      }
                    }
                    """
    )))
    @GetMapping("/isDisplayed")
    public ResponseEntity<ApiResult<Page<AdminNotifyFindDTO>>> findAllByDisplayedFalse(@ParameterObject
                                                                                      @PageableDefault(sort = "createdAt", direction = DESC, page = 0, size = 5)
                                                                  Pageable pageable) {
        Page<AdminNotifyFindDTO> all = notifyService.findAllByIsDisplayedFalse(pageable);
        return ResponseEntity.status(HttpStatus.OK)
                .body(success("최초 알림 조회", all));
    }

    @Operation(
            summary = "전체 알림 읽음 처리",
            description = "내 정보에서 읽지 않은 알림을 체크박스로 전체 읽음 처리"
    )
    @ApiResponse(
            responseCode = "200", description = "전체 알림 읽음 처리",
            content = @Content(schema = @Schema(implementation = ApiResult.class),
            examples = @ExampleObject(
                    name = "전체 알림 수정 예시",
                    summary = "전체 알림 수정 예제",
                    value = """
                            {
                              "status": 200,
                              "message": "알림 읽음 처리",
                              "timestamp": "2025-04-09T13:22:16.084887"
                            }
                            """
            )
            )
    )
    @PatchMapping
    public ResponseEntity<ApiResult<String>> markAsReadTrue(@RequestBody List<String> ids) {
        notifyService.markAsRead(ids);
        return ResponseEntity.status(HttpStatus.OK)
                .body(success("알림 읽음 처리"));
    }

    @Operation(
            summary = "알림 중복 호출 방지",
            description = "초기에 한번 랜더링 된 알림창은 다시 띄우지 않기 위한 API"
    )
    @ApiResponse(responseCode = "200", description = "알림 중복 호출 방지",
    content = @Content(schema = @Schema(implementation = ApiResult.class),
        examples = @ExampleObject(
                name = "알림 중복 호출 방지 업데이트 후 응답 예시",
                summary = "알림 중복 호출 방지 업데이트 후 응답 예제",
                value = """
                        {
                          "status": 200,
                          "message": "알림 업데이트 완료",
                          "timestamp": "2025-04-09T14:12:05.845506"
                        }
                        """
                )
        )
    )
    @PatchMapping("/isDisplayed")
    public ResponseEntity<ApiResult<String>> updatedByDisPlayed(@RequestBody List<String> notifyIds) {
        notifyService.updatedDisplayedTrue(notifyIds);
        return ResponseEntity.status(HttpStatus.OK).body(success("알림 업데이트 완료"));
    }
}
