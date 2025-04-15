package kr.co.pei.pei_app.application.service.s3;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface S3Service {

    List<String> imageUpload(List<MultipartFile> file) throws IOException;
}
