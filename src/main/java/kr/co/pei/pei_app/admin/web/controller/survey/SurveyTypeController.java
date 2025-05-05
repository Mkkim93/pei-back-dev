package kr.co.pei.pei_app.admin.web.controller.survey;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.pei.pei_app.admin.application.dto.api.ApiResult;
import kr.co.pei.pei_app.admin.application.dto.surveys.type.*;
import kr.co.pei.pei_app.admin.application.service.survey.SurveyCommonService;
import kr.co.pei.pei_app.admin.application.service.survey.SurveyTypeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.data.domain.Sort.Direction;

@Slf4j
@Tag(name = "SURVEY_TYPE_API", description = "관리자가 설문 유형을 관리하기 위한 API")
@RequestMapping("/api/survey-type")
@RestController
@RequiredArgsConstructor
public class SurveyTypeController {

    private final SurveyTypeService service;
    private final SurveyCommonService commonService;

    @GetMapping
    public ResponseEntity<ApiResult<Page<AdminFindTypeDTO>>> findPages(@ParameterObject @PageableDefault(
            page = 0, size = 10, sort = "name", direction = Direction.ASC) Pageable pageable,
                                                                       @RequestParam(value = "isPublic", required = false) boolean isPublic) {
        Page<AdminFindTypeDTO> pages = commonService.findMyHosTypeSurveyPage(pageable, isPublic);
        return ResponseEntity.status(HttpStatus.OK.value())
                .body(ApiResult.success("설문 유형 조회 성공", pages));
    }

    @GetMapping("/list")
    public ResponseEntity<ApiResult<List<AdminFindTypeDTO>>> findList(@RequestParam(value = "isPublic", required = false) boolean isPublic) {
        log.info("isPublic: {}", isPublic);
        List<AdminFindTypeDTO> list = commonService.findList(isPublic);
        return ResponseEntity.status(HttpStatus.OK.value())
                .body(ApiResult.success("설문 유형 리스트 조회 성공", list));
    }

    @GetMapping("/usang")
    public ResponseEntity<ApiResult<Page<AdminTypeUsageDTO>>> findUsagePage(@ParameterObject @PageableDefault(
            page = 0, size = 10, sort = "createdAt", direction = Direction.ASC) Pageable pageable, @RequestParam(value = "isPublic", required = false) boolean isPublic) {
        Page<AdminTypeUsageDTO> usagePage = commonService.findUsagePage(pageable, isPublic);
        return ResponseEntity.status(HttpStatus.OK.value()).body(ApiResult.success("설문 양식 통계 페이징 조회 성공", usagePage));
    }

    @Operation(summary = "최근 수정된 양식 목록", description = "최근 변경일 기준으로 수정된 양식 조회 (내 병원 / 공통)")
    @GetMapping("/recent")
    public ResponseEntity<ApiResult<Page<AdminFindTypeRecentDTO>>> recent(@ParameterObject @PageableDefault(
            page = 0, size = 10, sort = "updatedAt", direction = Direction.ASC) Pageable pageable, @RequestParam(value = "isPublic", required = false) boolean isPublic) {
        Page<AdminFindTypeRecentDTO> recentPage = commonService.findRecentPage(pageable, isPublic);
        return ResponseEntity.status(HttpStatus.OK.value()).body(ApiResult.success("수정된 양식 조회 성공", recentPage));
    }

    @Operation(summary = "진행 중인 설문 목록", description = "진행 중인 설문을 내병원/공통 으로 조회")
    @GetMapping("/status")
    public ResponseEntity<ApiResult<Page<AdminTypeStatusDTO>>> findStatusPage(@ParameterObject @PageableDefault(
      page = 0, size = 5, sort = "createdAt", direction = Direction.DESC) Pageable pageable,
                                                                              @RequestParam(value = "isPublic", required = false) boolean isPublic,
                                                                              @RequestParam(value = "status", required = false) String status) {
        Page<AdminTypeStatusDTO> pages = commonService.findSurveyStatusPage(pageable, isPublic, status);
        log.info("내병원 진행중인 설문 조회: {}", pages.getContent());
        return ResponseEntity.status(HttpStatus.OK.value()).body(ApiResult.success("설문 상태 조회 성공", pages));
    }


    @PostMapping("/add")
    public ResponseEntity<ApiResult<String>> addTypes(@RequestBody List<String> names) {
        service.saveType(names);
        return ResponseEntity.status(HttpStatus.CREATED.value()).body(ApiResult.success("등록 성공"));
    }

    @DeleteMapping
    public ResponseEntity<ApiResult<Boolean>> deleted(@RequestBody List<Long> ids) {
        service.deletedTypes(ids);
        return ResponseEntity.status(HttpStatus.OK.value())
                .body(ApiResult.success("삭제 성공", true));
    }

    @PatchMapping("/recover")
    public ResponseEntity<ApiResult<Boolean>> recovered(@RequestBody List<Long> ids) {
        service.recoveredTypes(ids);
        return ResponseEntity.status(HttpStatus.OK.value())
                .body(ApiResult.success("복구 성공", true));
    }

    @PatchMapping("/name")
    public ResponseEntity<ApiResult<String>> update(@RequestBody AdminUpdateTypeDTO adminUpdateTypeDTO) {
        service.updatedType(adminUpdateTypeDTO);
        return ResponseEntity.status(HttpStatus.OK.value()).body(ApiResult.success("설문 유형 수정 성공"));
    }


}
