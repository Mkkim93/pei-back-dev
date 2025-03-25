package kr.co.pei.pei_app.web.controller.log;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.pei.pei_app.application.dto.api.ApiResult;
import kr.co.pei.pei_app.application.dto.log.LogResponseDTO;
import kr.co.pei.pei_app.application.service.log.LogService;
import kr.co.pei.pei_app.config.exception.ErrorResult;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "LOG_API", description = "사용자 활동 로그 기록 API")
@RestController
@RequestMapping("/api/log")
@RequiredArgsConstructor
public class LogController {

    private final LogService logService;

    @Operation(summary = "로그 목록 조회", description = "사용자 전체 로그 기록 조회")
    @ApiResponse(responseCode = "200", description = "로그 기록 조회 성공",
            content = @Content(schema = @Schema(implementation = ApiResult.class)))
    @GetMapping
    public ResponseEntity<ApiResult<Page<LogResponseDTO>>> findAll(@ParameterObject @PageableDefault(
            sort = "createdAt", direction = Sort.Direction.ASC) Pageable pageable) {

        Page<LogResponseDTO> logList = logService.findAll(pageable);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResult.success("로그 기록 응답", logList));
    }

    @Operation(summary = "로그 기록 삭제", description = "사용자 단일 로그 기록 논리 삭제(단일)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그 기록 삭제 완료",
                    content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류로 인해 로그 삭제 실패",
                    content = @Content(schema = @Schema(implementation = ErrorResult.class)))
    })
    @PatchMapping
    public ResponseEntity<ApiResult<String>> deleted(@Parameter(description = "로그 ID", example = "PK") @RequestParam("logId") Long logId) {
        boolean delete = logService.delete(logId);
        if (!delete) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResult.error(
                            HttpStatus.BAD_REQUEST.value(),
                            "해당 로그가 존재하지 않거나 알 수 없는 이유로 인해 삭제에 실패 하였습니다.",
                            "LOG_DELETED_FAILED"));
        }
        return ResponseEntity.ok(ApiResult.success("로그가 삭제 되었습니다."));
    }
}
