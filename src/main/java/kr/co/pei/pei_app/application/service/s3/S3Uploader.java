package kr.co.pei.pei_app.application.service.s3;

import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import kr.co.pei.pei_app.config.s3.S3Config;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3Uploader implements S3Service {

    private final S3Config s3Config;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${file.upload.dir}")
    private String localLocation;

    @Override
    public List<String> imageUpload(List<MultipartFile> files) throws IOException {

        List<String> urls = new ArrayList<>();

        for (MultipartFile file : files) {
            String fileName = file.getOriginalFilename();

            if (fileName == null || !fileName.contains(".")) {
                throw new IllegalArgumentException("파일명이 유효하지 않습니다.");
            }

            String ext = fileName.substring(fileName.lastIndexOf("."));
            String uuidFileName = UUID.randomUUID() + ext;
            String localPath = localLocation + uuidFileName;

            File localFile = new File(localPath);
            file.transferTo(localFile);
            log.info("로컬 파일 저장 완료: {}", localPath);

            s3Config.amazonS3Client().putObject(
                    new PutObjectRequest(bucket, uuidFileName, localFile)
                            .withCannedAcl(CannedAccessControlList.PublicRead)
            );

            String url = s3Config.amazonS3Client().getUrl(bucket, uuidFileName).toString();
            urls.add(url);

            if (localFile.delete()) {
                log.info("로컬 파일 삭제 완료: {}", localPath);
            } else {
                log.warn("로컬 파일 삭제 실패: {}", localPath);
            }
        }
        return urls;
    }
}
