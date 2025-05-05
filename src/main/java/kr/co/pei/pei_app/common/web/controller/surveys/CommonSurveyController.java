package kr.co.pei.pei_app.common.web.controller.surveys;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.pei.pei_app.admin.application.dto.api.ApiResult;
import kr.co.pei.pei_app.admin.application.dto.surveys.survey.AdminSurveyDetailDTO;
import kr.co.pei.pei_app.common.application.dto.surveys.survey.CommonDetailSurveyDTO;
import kr.co.pei.pei_app.common.application.dto.surveys.survey.CommonFindSurveyListDTO;
import kr.co.pei.pei_app.common.application.service.surveys.CommonSurveyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Tag(name = "COMMON_SURVEY_API", description = "사용자가 특정 병원 설문에 대한 응답 API")
@RestController
@RequestMapping("/api/common-survey")
@RequiredArgsConstructor
public class CommonSurveyController {

    private final CommonSurveyService service;

    // 선택한 병원에서 진행중인 설문 조회
    @Operation(summary = "진행 중인 설문 조회", description = "선택한 병원에서 현재 진행중인 설문양식 페이징 조회")
    @GetMapping
    public ResponseEntity<ApiResult<Page<CommonFindSurveyListDTO>>> findActivePage(
            @ParameterObject @PageableDefault(page = 0, size = 5, sort = "openAt", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam("hospitalId") Long hospitalId, @RequestParam(value = "status") String status) {

        Page<CommonFindSurveyListDTO> page = service.findAllActiveSurveyPage(pageable, hospitalId, status);

        return ResponseEntity.status(HttpStatus.OK.value()).body(ApiResult.success("진행 중 설문 조회 성공", page));
    }

    @Operation(summary = "설문 양식 조회", description = "사용자가 선택한 설문양식 응답 API")
    @GetMapping
    public ResponseEntity<ApiResult<CommonDetailSurveyDTO>> findSurveyTemplate(@RequestParam("id") Long id) {
//        AdminSurveyDetailDTO adminSurveyDetailDTO = surveyCommonService.findSurveyDetail(id);

//        log.info("surveyJson: {}", adminSurveyDetailDTO);
        return ResponseEntity.status(HttpStatus.OK.value())
                .body(ApiResult.success("상세 조회 성공", null));
    }


}
