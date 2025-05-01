package kr.co.pei.pei_app.web.controller.survey;


import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.pei.pei_app.application.dto.api.ApiResult;
import kr.co.pei.pei_app.application.dto.surveys.survey.CreateSurveyDTO;
import kr.co.pei.pei_app.application.dto.surveys.survey.DetailSurveyDTO;
import kr.co.pei.pei_app.application.dto.surveys.survey.FindSurveyDTO;
import kr.co.pei.pei_app.application.dto.surveys.survey.UpdateSurveyDTO;
import kr.co.pei.pei_app.application.service.survey.SurveyCommonService;
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

import static org.springframework.data.domain.Sort.*;

@Slf4j
@Tag(name = "SURVEY_API", description = "설문지 응답 관련 API")
@RestController
@RequestMapping("/api/survey")
@RequiredArgsConstructor
public class SurveyController {

    private final SurveyCommonService surveyCommonService;

    @Operation(summary = "양식 조회", description = "클라이언트에서 survey 테이블의 title 기준으로 json 파일을 요청한다.")
    @GetMapping
    public ResponseEntity<ApiResult<DetailSurveyDTO>> findSurveyTemplate(@RequestParam("id") Long id) {
        DetailSurveyDTO detailSurveyDTO = surveyCommonService.findSurveyDetail(id);
        log.info("surveyJson: {}", detailSurveyDTO);
        return ResponseEntity.status(HttpStatus.OK.value())
                .body(ApiResult.success("상세 조회 성공", detailSurveyDTO));
    }

    @Operation(summary = "사용자가 소속된 병원 설문 양식 조회", description = "사용자 또는 다른 관리자가 작성한 현재 소속 병원의 양식만 조회")
    @GetMapping("/hospital")
    public ResponseEntity<ApiResult<Page<FindSurveyDTO>>> findMySurveyPage(@ParameterObject @PageableDefault(page = 0, size = 5, sort = "createdAt",
            direction = Direction.ASC) Pageable pageable) {
        Page<FindSurveyDTO> result = surveyCommonService.findMyPage(pageable);
        return ResponseEntity.status(HttpStatus.OK.value()).body(ApiResult.success("소속 병원 양식 조회 성공", result));
    }

    @PostMapping
    public ResponseEntity<ApiResult<String>> save(@RequestBody CreateSurveyDTO createSurveyDTO) {
        log.info("클라이언트에서 받은 dto (내부 json 포함) : {} ", createSurveyDTO);
        surveyCommonService.saveSurveyTemplate(createSurveyDTO);
        return ResponseEntity.status(HttpStatus.CREATED.value()).body(ApiResult.success("양식이 성공적으로 등록 되었습니다."));
    }

    @PostMapping("/update")
    public ResponseEntity<ApiResult<String>> update(@RequestBody UpdateSurveyDTO updateSurveyDTO) throws JsonProcessingException {
        log.info("updateDTO: {}", updateSurveyDTO);
        surveyCommonService.surveyUpdate(updateSurveyDTO);
        return ResponseEntity.status(HttpStatus.OK.value()).body(ApiResult.success("업데이트 성공"));
    }

    @GetMapping("/category")
    public ResponseEntity<ApiResult<Map<CategoryType, String>>> resCategory() {
        Map<CategoryType, String> categoryList = surveyCommonService.findCategory();
        return ResponseEntity.status(HttpStatus.OK.value()).body(ApiResult.success("카테고리 조회 성공", categoryList));
    }
}