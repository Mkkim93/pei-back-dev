package kr.co.pei.pei_app.admin.web.controller.s3;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.pei.pei_app.admin.application.dto.api.ApiResult;
import kr.co.pei.pei_app.admin.application.dto.file.AdminFileBoardDTO;
import kr.co.pei.pei_app.admin.application.service.s3.S3ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static kr.co.pei.pei_app.admin.application.dto.api.ApiResult.success;

@Slf4j
@Tag(name = "S3_FILE_UPLOAD_API")
@RestController
@RequestMapping("/api/s3upload")
@RequiredArgsConstructor
public class S3Controller {

    private final S3ServiceImpl s3ServiceImpl;

    @Operation(summary = "파일 업로드", description = "S3 에 업로드할 이미지 요청 컨트롤러")
    @PostMapping
    public ResponseEntity<ApiResult<List<AdminFileBoardDTO>>> uploader(@RequestParam("files") List<MultipartFile> files) throws IOException {
        List<AdminFileBoardDTO> fileMetaList = s3ServiceImpl.fileUpload(files);
        log.info("컨트롤러 파일 응답 객체: {}", fileMetaList);
        return ResponseEntity.ok(success("파일 등록 성공", fileMetaList));
    }
}
