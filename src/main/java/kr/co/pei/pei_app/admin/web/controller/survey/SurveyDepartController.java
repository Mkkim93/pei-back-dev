package kr.co.pei.pei_app.admin.web.controller.survey;

import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.pei.pei_app.admin.application.dto.api.ApiResult;
import kr.co.pei.pei_app.admin.application.dto.surveys.depart.AdminFindDepartDTO;
import kr.co.pei.pei_app.admin.application.dto.surveys.depart.AdminUpdateDepartDTO;
import kr.co.pei.pei_app.admin.application.service.survey.SurveyDepartService;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static kr.co.pei.pei_app.admin.application.dto.api.ApiResult.success;
import static org.springframework.data.domain.Sort.Direction;

@Tag(name = "SURVEY_DEPART_API", description = "관리자가 진료과를 관리하기 위한 API")
@RestController
@RequestMapping("/api/survey-depart")
@RequiredArgsConstructor
public class SurveyDepartController {

    private final SurveyDepartService service;

    @PostMapping("/upload")
    public ResponseEntity<ApiResult<String>> uploadDepartExcel(@RequestParam("file") MultipartFile file) throws IOException {
        service.saveDepartsForExcel(file);
        return ResponseEntity.ok(success("진료과 등록 성공"));
    }

    @GetMapping
    public ResponseEntity<ApiResult<?>> findPages(@ParameterObject @PageableDefault(
            page = 0, size = 20, sort = "name", direction = Direction.ASC) Pageable pageable,
                                                  @RequestParam(value = "all", required = false) Boolean all) {

        if (!all) {
            Page<AdminFindDepartDTO> pages = service.findPages(pageable);
            return ResponseEntity.status(HttpStatus.OK.value())
                    .body(success("진료과 페이징 조회 성공", pages));
        }

        List<AdminFindDepartDTO> list = service.findList();
        return ResponseEntity.status(HttpStatus.OK.value()).body(ApiResult.success("진료과 리스트 조회 성공", list));
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResult<String>> save(@RequestBody List<String> departNames) {
        service.saveDepart(departNames);
        return ResponseEntity.status(HttpStatus.CREATED.value()).body(success("등록 성공"));
    }

    @DeleteMapping
    public ResponseEntity<ApiResult<Boolean>> delete(@RequestBody List<Long> ids) {
        service.deletedDeparts(ids);
        return ResponseEntity.status(HttpStatus.OK.value())
                .body(success("삭제 완료", true));
    }

    @PatchMapping("/recover")
    public ResponseEntity<ApiResult<Boolean>> recover(@RequestBody List<Long> ids) {
        service.recoverDeparts(ids);
        return ResponseEntity.status(HttpStatus.OK.value())
                .body(success("복구 성공", true));
    }

    @PatchMapping("/name")
    public ResponseEntity<ApiResult<String>> update(@RequestBody AdminUpdateDepartDTO adminUpdateDepartDTO) {
        service.updateDeparts(adminUpdateDepartDTO);
        return ResponseEntity.status(HttpStatus.OK.value()).body(success("진료과 수정 성공"));
    }
}
