package kr.co.pei.pei_app.admin.web.controller.schedule;

import io.swagger.v3.oas.annotations.Operation;
import kr.co.pei.pei_app.admin.application.dto.api.ApiResult;
import kr.co.pei.pei_app.admin.application.dto.schedule.AdminCreateScheduleDTO;
import kr.co.pei.pei_app.admin.application.dto.schedule.AdminFindScheduleDTO;
import kr.co.pei.pei_app.admin.application.dto.schedule.AdminScheduleUpdateDTO;
import kr.co.pei.pei_app.admin.application.service.schedule.ScheduleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/schedule")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService service;

    @Operation(summary = "모든 일정 조회 임시 테스트", description = "임시 테스트 API")
    @GetMapping
    public ResponseEntity<ApiResult<List<AdminFindScheduleDTO>>> findAll() {
        List<AdminFindScheduleDTO> list = service.findAll();
        return ResponseEntity.status(HttpStatus.OK.value()).body(ApiResult.success("일정 전체 리스트 조회 성공", list));
    }

    @Operation(summary = "병원의 일정을 현재 로그인한 사용자를 기준으로 등록", description = "관리자가 일정 등록을 위한 API")
    @PostMapping
    public ResponseEntity<ApiResult<String>> save(@RequestBody AdminCreateScheduleDTO dto) {
        service.save(dto);
        return ResponseEntity.status(HttpStatus.CREATED.value()).body(ApiResult.success("일정이 성공적으로 등록 되었습니다."));
    }

    @Operation(summary = "일정 삭제", description = "일정 삭제(is_deleted = true) API")
    @DeleteMapping("{id}")
    public ResponseEntity<ApiResult<String>> delete(@PathVariable("id") Long id) {
        service.delete(id);
        return ResponseEntity.status(HttpStatus.OK.value()).body(ApiResult.success("일정이 삭제 되었습니다."));
    }

    @Operation(summary = "일정 수정", description = "일정 수정 API")
    @PatchMapping
    public ResponseEntity<ApiResult<String>> update(@RequestBody AdminScheduleUpdateDTO dto) {
        log.info("수정할 dto : {}", dto);
        service.update(dto);
        return ResponseEntity.status(HttpStatus.OK.value()).body(ApiResult.success("일정이 수정 되었습니다."));
    }
}
