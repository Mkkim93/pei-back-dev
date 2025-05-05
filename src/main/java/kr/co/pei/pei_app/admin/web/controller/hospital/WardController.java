package kr.co.pei.pei_app.admin.web.controller.hospital;

import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.pei.pei_app.admin.application.dto.api.ApiResult;
import kr.co.pei.pei_app.admin.application.service.hospital.WardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "WARD_API", description = "관리자가 병동 등록을 위한 API")
@RestController
@RequestMapping("/api/ward")
@RequiredArgsConstructor
public class WardController {

    private final WardService service;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResult<String>> uploadWardExcel(@RequestParam("file") MultipartFile file) {
        service.saveWardForExcel(file);
        return ResponseEntity.status(HttpStatus.OK.value()).body(ApiResult.success("병동 정보가 성공적으로 등록되었습니다."));
    }

}
