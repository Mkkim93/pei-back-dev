package kr.co.pei.pei_app.web.controller.s3;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.pei.pei_app.application.service.s3.S3Uploader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Tag(name = "S3_FILE_UPLOAD_API")
@RestController
@RequestMapping("/api/upload")
@RequiredArgsConstructor
public class FileUploaderController {

    private final S3Uploader s3Uploader;

    @Operation(summary = "파일 업로드", description = "S3 에 업로드할 이미지 요청 컨트롤러")
    @PostMapping
    public ResponseEntity<List<String>> uploader(@RequestParam("files") List<MultipartFile> file) throws IOException {
        return ResponseEntity.ok(s3Uploader.imageUpload(file));
    }
}
