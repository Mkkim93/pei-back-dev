package kr.co.pei.pei_app.web.controller.survey;

import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.pei.pei_app.application.dto.api.ApiResult;
import kr.co.pei.pei_app.application.dto.survey.depart.FindDepartDTO;
import kr.co.pei.pei_app.application.dto.survey.depart.UpdateDepartDTO;
import kr.co.pei.pei_app.application.service.survey.SurveyDepartService;
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

import static kr.co.pei.pei_app.application.dto.api.ApiResult.*;
import static org.springframework.data.domain.Sort.*;

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
    public ResponseEntity<ApiResult<Page<FindDepartDTO>>> findDepartPages(@ParameterObject @PageableDefault(
            page = 0, size = 20, sort = "name", direction = Direction.ASC) Pageable pageable) {

        Page<FindDepartDTO> pages = service.findPages(pageable);

        return ResponseEntity.status(HttpStatus.OK.value())
                .body(success("진료과 조회 성공", pages));
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResult<String>> addDeparts(@RequestBody List<String> departNames) {
        service.saveDepart(departNames);
        return ResponseEntity.status(HttpStatus.CREATED.value()).body(success("등록 성공"));
    }

    @DeleteMapping
    public ResponseEntity<ApiResult<Boolean>> deleted(@RequestBody List<Long> ids) {

        boolean result = service.deletedDeparts(ids);

        if (!result) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST.value())
                    .body(error("삭제 실패", false));
        }
        return ResponseEntity.status(HttpStatus.OK.value())
                .body(success("삭제 완료", true));
    }

    @PatchMapping("/recover")
    public ResponseEntity<ApiResult<Boolean>> recovered(@RequestBody List<Long> ids) {

        boolean result = service.recoverDeparts(ids);

        if (!result) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST.value())
                    .body(error("복구 실패",false));
        }
        return ResponseEntity.status(HttpStatus.OK.value())
                .body(success("복구 성공", true));
    }

    @PatchMapping("/name")
    public ResponseEntity<ApiResult<String>> update(@RequestBody UpdateDepartDTO updateDepartDTO) {
        service.updateDeparts(updateDepartDTO);
        return ResponseEntity.status(HttpStatus.OK.value()).body(success("진료과 수정 성공"));
    }
}
