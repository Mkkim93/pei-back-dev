package kr.co.pei.pei_app.common.web.controller.hopital;

import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.pei.pei_app.admin.application.dto.api.ApiResult;
import kr.co.pei.pei_app.common.application.dto.hospital.FindHospitalDTO;
import kr.co.pei.pei_app.common.application.service.hospital.CommonHospitalService;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "USER_HOSPITAL_API", description = "일반 사용자가 병원 선택을 하기 위한 API")
@RequestMapping("/api/com-hospital")
@RestController
@RequiredArgsConstructor
public class CommonHospitalController {

    private final CommonHospitalService service;

    @GetMapping
    public ResponseEntity<ApiResult<Page<FindHospitalDTO>>> findPages(@ParameterObject @PageableDefault(page = 0, size = 10, sort = "name", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<FindHospitalDTO> pages = service.findPages(pageable);
        return ResponseEntity.status(HttpStatus.OK.value())
                .body(ApiResult.success("조회 성공", pages));
    }
}
