package kr.co.pei.pei_app.admin.web.controller.survey;

import jakarta.validation.Valid;
import kr.co.pei.pei_app.admin.application.dto.api.ApiResult;
import kr.co.pei.pei_app.admin.application.dto.surveys.part.AdminCreatePartDTO;
import kr.co.pei.pei_app.admin.application.service.survey.SurveyPartService;
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
    public ResponseEntity<ApiResult<String>> createPartInfo(@Valid @RequestBody AdminCreatePartDTO adminCreatePartDTO) {
        service.savePart(adminCreatePartDTO);
        return ResponseEntity.status(HttpStatus.CREATED.value()).body(ApiResult.success("사용자 정보 등록 성공"));
    }
}
