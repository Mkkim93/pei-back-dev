package kr.co.pei.pei_app.admin.web.controller.survey;


import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.pei.pei_app.admin.application.dto.api.ApiResult;
import kr.co.pei.pei_app.admin.application.dto.surveys.survey.AdminCreateSurveyDTO;
import kr.co.pei.pei_app.admin.application.dto.surveys.survey.AdminFindSurveyDTO;
import kr.co.pei.pei_app.admin.application.dto.surveys.survey.AdminSurveyDetailDTO;
import kr.co.pei.pei_app.admin.application.dto.surveys.survey.AdminUpdateSurveyDTO;
import kr.co.pei.pei_app.admin.application.service.survey.SurveyCommonService;
import kr.co.pei.pei_app.domain.entity.survey.enums.CategoryType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static org.springframework.data.domain.Sort.Direction;

@Slf4j
@Tag(name = "SURVEY_API", description = "설문지 응답 관련 API")
@RestController
@RequestMapping("/api/survey")
@RequiredArgsConstructor
public class SurveyController {

    private final SurveyCommonService surveyCommonService;

    @Operation(summary = "양식 조회", description = "클라이언트에서 survey 테이블의 title 기준으로 json 파일을 요청한다.")
    @GetMapping
    public ResponseEntity<ApiResult<AdminSurveyDetailDTO>> findSurveyTemplate(@RequestParam("id") Long id) {
        AdminSurveyDetailDTO adminSurveyDetailDTO = surveyCommonService.findSurveyDetail(id);
        log.info("surveyJson: {}", adminSurveyDetailDTO);
        return ResponseEntity.status(HttpStatus.OK.value())
                .body(ApiResult.success("상세 조회 성공", adminSurveyDetailDTO));
    }

    @Operation(summary = "사용자가 소속된 병원 설문 양식 조회", description = "사용자 또는 다른 관리자가 작성한 현재 소속 병원의 양식만 조회")
    @GetMapping("/hospital")
    public ResponseEntity<ApiResult<Page<AdminFindSurveyDTO>>> findMySurveyPage(@ParameterObject @PageableDefault(page = 0, size = 5, sort = "createdAt",
            direction = Direction.ASC) Pageable pageable) {
        Page<AdminFindSurveyDTO> result = surveyCommonService.findMyPage(pageable);
        return ResponseEntity.status(HttpStatus.OK.value()).body(ApiResult.success("소속 병원 양식 조회 성공", result));
    }

    @PostMapping
    public ResponseEntity<ApiResult<String>> save(@RequestBody AdminCreateSurveyDTO adminCreateSurveyDTO) {
        log.info("클라이언트에서 받은 dto (내부 json 포함) : {} ", adminCreateSurveyDTO);
        surveyCommonService.saveSurveyTemplate(adminCreateSurveyDTO);
        return ResponseEntity.status(HttpStatus.CREATED.value()).body(ApiResult.success("양식이 성공적으로 등록 되었습니다."));
    }

    @PostMapping("/update")
    public ResponseEntity<ApiResult<String>> update(@RequestBody AdminUpdateSurveyDTO adminUpdateSurveyDTO) throws JsonProcessingException {
        log.info("updateDTO: {}", adminUpdateSurveyDTO);
        surveyCommonService.surveyUpdate(adminUpdateSurveyDTO);
        return ResponseEntity.status(HttpStatus.OK.value()).body(ApiResult.success("양식이 정상적으로 업데이트 되었습니다."));
    }

    @GetMapping("/category")
    public ResponseEntity<ApiResult<Map<CategoryType, String>>> resCategory() {
        Map<CategoryType, String> categoryList = surveyCommonService.findCategory();
        return ResponseEntity.status(HttpStatus.OK.value()).body(ApiResult.success("카테고리 조회 성공", categoryList));
    }
}