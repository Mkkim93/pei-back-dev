package kr.co.pei.pei_app.web.controller.hospital;

import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.pei.pei_app.application.dto.api.ApiResult;
import kr.co.pei.pei_app.application.dto.hospital.FindHosDTO;
import kr.co.pei.pei_app.application.dto.hospital.UpdateHosDTO;
import kr.co.pei.pei_app.application.service.hospital.HospitalService;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.data.domain.Sort.*;

@Tag(name = "HOSPITAL_API", description = "병원 데이터 관련 API")
@RestController
@RequestMapping("/api/hospital")
@RequiredArgsConstructor
public class HospitalController {

    private final HospitalService service;

    @GetMapping
    public ResponseEntity<ApiResult<Page<FindHosDTO>>> findPages(@ParameterObject @PageableDefault(page = 0, size = 10, sort = "name", direction = Direction.ASC) Pageable pageable) {
        Page<FindHosDTO> pages = service.findPages(pageable);
        return ResponseEntity.status(HttpStatus.OK.value())
                .body(ApiResult.success("조회 성공", pages));
    }

    @PostMapping
    public ResponseEntity<ApiResult<String>> save(@RequestParam("name") String name) {
        service.saveHospital(name);
        return ResponseEntity.status(HttpStatus.CREATED.value()).body(ApiResult.success("등록 성공"));
    }

    @PatchMapping("/name")
    public ResponseEntity<ApiResult<String>> update(@RequestBody UpdateHosDTO updateHosDTO) {
        service.updateName(updateHosDTO);
        return ResponseEntity.status(HttpStatus.OK.value()).body(ApiResult.success("데이터 수정 성공"));
    }

    @DeleteMapping
    public ResponseEntity<ApiResult<String>> delete(@RequestParam("id") Long id) {
        service.deletedHospital(id);
        return ResponseEntity.status(HttpStatus.OK.value()).body(ApiResult.success("삭제 성공"));
    }

    @PatchMapping("/recover")
    public ResponseEntity<ApiResult<String>> recover(@RequestParam("id") Long id) {
        service.recoveredHospital(id);
        return ResponseEntity.status(HttpStatus.OK.value()).body(ApiResult.success("복구 성공"));
    }
}
