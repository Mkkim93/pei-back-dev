package kr.co.pei.pei_app.web.controller.log;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.pei.pei_app.application.dto.api.ApiResult;
import kr.co.pei.pei_app.application.dto.log.LogResponseDTO;
import kr.co.pei.pei_app.application.service.log.LogService;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.data.domain.Sort.Direction.*;

@Tag(name = "LOG_API", description = "사용자 활동 로그 기록 API")
@RestController
@RequestMapping("/api/log")
@RequiredArgsConstructor
public class LogController {

    private final LogService logService;

    @Operation(summary = "로그 목록 조회", description = "사용자 전체 로그 기록 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그 기록 조회 성공",
                    content = @Content(schema = @Schema(implementation = ApiResult.class),
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "로그 기록 응답 성공",
                                    summary = "최근 활동 로그 생성 날짜 기준 오름차순 10개 응답",
                                    value = """
                                            {
                                                "status": 200,
                                                "message": "로그 기록 응답",
                                                "timestamp": "2025-03-26T18:06:10.67577",
                                                "data": {
                                                    "content": [
                                                        {
                                                            "id": 7,
                                                            "action": "게시글 업데이트",
                                                            "description": {
                                                                "title": "게시글 수정 (로그테스트7)",
                                                                "boardId": 20,
                                                                "content": "게시글을 수정합니다."
                                                            },
                                                            "createdAt": "2025-03-19T20:48:06.627906",
                                                            "users": "관리자1"
                                                        },
                                                        {
                                                            "id": 8,
                                                            "action": "게시글 업데이트",
                                                            "description": {
                                                                "title": "게시글 수정 (로그테스트8)",
                                                                "boardId": 20,
                                                                "content": "게시글을 수정합니당."
                                                            },
                                                            "createdAt": "2025-03-19T20:48:37.562604",
                                                            "users": "관리자1"
                                                        },
                                                        {
                                                            "id": 9,
                                                            "action": "게시글 업데이트",
                                                            "description": {
                                                                "title": "게시글 수정 (로그테스트9)",
                                                                "boardId": 20,
                                                                "content": "게시글을 수정합니다."
                                                            },
                                                            "createdAt": "2025-03-19T20:50:33.773698",
                                                            "users": "관리자1"
                                                        }
                                                    ],
                                                    "page": {
                                                        "size": 10,
                                                        "number": 0,
                                                        "totalElements": 3,
                                                        "totalPages": 1
                                                    }
                                                }
                                            }
                                            """
                            )
                    )
            )
    })
    @GetMapping
    public ResponseEntity<ApiResult<Page<LogResponseDTO>>> findAll(@ParameterObject @PageableDefault(
            sort = "createdAt", direction = DESC) Pageable pageable) {

        Page<LogResponseDTO> logList = logService.findAll(pageable);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResult.success("로그 기록 응답", logList));
    }

    @Operation(summary = "로그 기록 삭제", description = "사용자 단일 로그 기록 논리 삭제(단일 삭제)")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "로그 기록 삭제 완료",
                    content = @Content(schema = @Schema(implementation = ApiResult.class),
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "로그 기록 삭제 성공 예시",
                                    summary = "로그 기록 삭제 성공 응답 예제",
                                    value = """
                                            {
                                                "status" : 200,
                                                "message" : "로그가 삭제 되었습니다.",
                                                "timestamp": "2025-03-26T18:06:10.67577"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류로 인해 로그 삭제 실패",
                    content = @Content(schema = @Schema(implementation = ApiResult.class),
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "로그 삭제 실패 예시 (서버 내부 오류)",
                                    summary = "로그 삭제 실패 응답 예제",
                                    value = """
                                            {
                                                "status" : 500,
                                                "message" : "죄송합니다. 현재 서버 오류로 인해 로그가 삭제되지 않았습니다. 다시 시도하시거나 관리자에게 문의 해주세요.",
                                                "timestamp": "2025-03-26T18:06:10.67577"
                                            }
                                            """
                            )
                    )
            )
    })
    @PatchMapping
    public ResponseEntity<ApiResult<String>> deleted(@Parameter(description = "로그 ID", example = "PK") @RequestParam("logId") Long logId) {
        boolean delete = logService.delete(logId);
        if (!delete) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResult.error(
                            HttpStatus.BAD_REQUEST.value(),
                            "죄송합니다. 현재 서버 오류로 인해 로그가 삭제되지 않았습니다. 다시 시도하시거나 관리자에게 문의 해주세요.",
                            "LOG_DELETED_FAILED"));
        }
        return ResponseEntity.ok(ApiResult.success("로그가 삭제 되었습니다."));
    }
}
