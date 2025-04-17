package kr.co.pei.pei_app.web.controller.file;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import kr.co.pei.pei_app.application.dto.api.ApiResult;
import kr.co.pei.pei_app.application.dto.file.FileDownLoadDTO;
import kr.co.pei.pei_app.application.service.file.FileStoreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriUtils;

import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;

@Slf4j
@RestController
@RequestMapping("/api/file")
@RequiredArgsConstructor
public class FileController {

    private final FileStoreService fileStoreService;

    @Operation(summary = "파일 다운로드", description = "파일 다운로드 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "파일 다운로드 성공", content = @Content(mediaType = "application/octet-stream")),
            @ApiResponse(responseCode = "404", description = "파일을 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류로 인한 파일 다운로드 실패")
    })
    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> download(@PathVariable("id") Long id) throws MalformedURLException {

        FileDownLoadDTO downLoadFile = fileStoreService.findById(id);
        String uploadFIleName = downLoadFile.getOrgName();

        UrlResource urlResource = new UrlResource(downLoadFile.getPath());
        String encodeFileName = UriUtils.encode(uploadFIleName, StandardCharsets.UTF_8);
        String contentDisposition = "attachment; fileName=\"" + encodeFileName + "\"";

        log.info("urlResource: {}", urlResource);
        log.info("encodeFileName: {}", encodeFileName);
        log.info("contentDisposition: {}", contentDisposition);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .body(urlResource);
    }

//    @Operation(summary = "파일 삭제", description = "파일 삭제를 위한 API")
//    @DeleteMapping
//    public ResponseEntity<ApiResult<String>> delete(@RequestParam("id") Long id) {
//
//        return null;
//    }
}
