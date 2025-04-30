package kr.co.pei.pei_app.web.controller.survey;

import jakarta.validation.Valid;
import kr.co.pei.pei_app.application.dto.api.ApiResult;
import kr.co.pei.pei_app.application.dto.surveys.part.CreatePartDTO;
import kr.co.pei.pei_app.application.service.survey.SurveyPartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/survey-part")
@RequiredArgsConstructor
public class SurveyPartController {

    private final SurveyPartService service;

    @PostMapping
    public ResponseEntity<ApiResult<String>> createPartInfo(@Valid @RequestBody CreatePartDTO createPartDTO) {
        service.savePart(createPartDTO);
        return ResponseEntity.status(HttpStatus.CREATED.value()).body(ApiResult.success("사용자 정보 등록 성공"));
    }
}
