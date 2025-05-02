package kr.co.pei.pei_app.admin.web.controller.survey;

import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.pei.pei_app.admin.application.dto.api.ApiResult;
import kr.co.pei.pei_app.admin.application.dto.surveys.type.AdminFindTypeDTO;
import kr.co.pei.pei_app.admin.application.dto.surveys.type.AdminUpdateTypeDTO;
import kr.co.pei.pei_app.admin.application.service.survey.SurveyCommonService;
import kr.co.pei.pei_app.admin.application.service.survey.SurveyTypeService;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.data.domain.Sort.Direction;

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

    @PostMapping("/add")
    public ResponseEntity<ApiResult<String>> addTypes(@RequestBody List<String> names) {
        service.saveType(names);
        return ResponseEntity.status(HttpStatus.CREATED.value()).body(ApiResult.success("등록 성공"));
    }

    @DeleteMapping
    public ResponseEntity<ApiResult<Boolean>> deleted(@RequestBody List<Long> ids) {
        boolean result = service.deletedTypes(ids);

        if (result) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST.value())
                    .body(ApiResult.error("삭제 실패",false));
        }
        return ResponseEntity.status(HttpStatus.OK.value())
                .body(ApiResult.success("삭제 성공", true));
    }

    @PatchMapping("/recover")
    public ResponseEntity<ApiResult<Boolean>> recovered(@RequestBody List<Long> ids) {

        boolean result = service.recoveredTypes(ids);

        if (!result) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST.value())
                    .body(ApiResult.error("복구 실패",false));
        }
        return ResponseEntity.status(HttpStatus.OK.value())
                .body(ApiResult.success("복구 성공", true));
    }

    @PatchMapping("/name")
    public ResponseEntity<ApiResult<String>> update(@RequestBody AdminUpdateTypeDTO adminUpdateTypeDTO) {
        service.updatedType(adminUpdateTypeDTO);
        return ResponseEntity.status(HttpStatus.OK.value()).body(ApiResult.success("설문 유형 수정 성공"));
    }


}
