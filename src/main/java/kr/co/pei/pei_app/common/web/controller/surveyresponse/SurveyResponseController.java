package kr.co.pei.pei_app.common.web.controller.surveyresponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.pei.pei_app.admin.application.dto.api.ApiResult;
import kr.co.pei.pei_app.admin.application.dto.surveys.response.SurveyResponseDetailDTO;
import kr.co.pei.pei_app.admin.application.service.survey.SurveyResponseService;
import kr.co.pei.pei_app.common.application.dto.surveyresponse.SurveyResponseCreateDTO;
import kr.co.pei.pei_app.common.application.dto.surveyresponse.SurveyResponseFindMetaDTO;
import kr.co.pei.pei_app.common.application.service.surveyresponse.SurveyResponseHandlerService;
import kr.co.pei.pei_app.common.application.service.surveys.CommonSurveyResponseService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Tag(name = "COMMON_SURVEY_RESPONSE_API", description = "사용자가 설문 응답을 보내기 위한 API")
@RestController
@RequestMapping("/api/survey-res")
@RequiredArgsConstructor
public class SurveyResponseController {

    private final SurveyResponseHandlerService handlerService;


    @Operation(summary = "진료과, 병동 리스트 조회", description = "설문자가 설문 작성 초기 데이터 입력을 위한 API")
    @GetMapping
    public ResponseEntity<ApiResult<SurveyResponseFindMetaDTO>> findListMetaData(@RequestParam("hospitalId") Long hospitalId) {
        SurveyResponseFindMetaDTO list = handlerService.findMetaList(hospitalId);
        log.info("list: {}", list);
        return ResponseEntity.status(HttpStatus.OK.value()).body(ApiResult.success("병동 진료과 리스트 조회 성공", list));
    }

    @Operation(summary = "설문 응답 저장", description = "환자가 작성한 설문 정보를 저장하기 위한 API")
    @PostMapping
    public ResponseEntity<ApiResult<String>> saveSurveyResponse(@RequestBody SurveyResponseCreateDTO createDTO) {
        log.info("작성한 설문 데이터 dto: {}", createDTO);
        handlerService.saveHandler(createDTO);
        return ResponseEntity.status(HttpStatus.CREATED.value()).body(ApiResult.success("설문에 참여해주셔서 감사합니다."));
    }

    @Operation(summary = "설문 상세 조회", description = "환자가 작성한 설문 데이터를 리스트형식으로 모두 응답하기 위한 API")
    @GetMapping("/detail")
    public ResponseEntity<ApiResult<List<SurveyResponseDetailDTO>>> findDetail(@RequestParam("surveyId") Long surveyId,
                                                                               @RequestParam("status") String status) {
        List<SurveyResponseDetailDTO> detail = handlerService.findDetail(surveyId, status);
        log.info("설문 상세: {} ", detail);
        return ResponseEntity.status(HttpStatus.OK.value()).body(ApiResult.success("통계 전용 데이터 조회 성공", detail));
    }
}
