package kr.co.pei.pei_app.application.service.s3;

import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import kr.co.pei.pei_app.application.dto.file.FileBoardDTO;
import kr.co.pei.pei_app.config.s3.S3Config;
import kr.co.pei.pei_app.domain.entity.file.FileStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3ServiceImpl implements S3Service {

    private final S3Config s3Config;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${file.upload.dir}")
    private String localLocation;

    @Override
    public List<FileBoardDTO> fileUpload(List<MultipartFile> files) throws IOException {

        List<FileBoardDTO> dtoList = new ArrayList<>();

        for (MultipartFile file : files) {
            FileBoardDTO dto = new FileBoardDTO();
            try (InputStream inputStream = file.getInputStream()) {
                String orgName = file.getOriginalFilename();

                if (orgName == null || !orgName.contains(".")) {
                    throw new IllegalArgumentException("파일명이 유효하지 않습니다.");
                }

                String ext = orgName.substring(orgName.lastIndexOf("."));
                String uuidFileName = UUID.randomUUID() + ext;

                ObjectMetadata metadata = new ObjectMetadata();
                metadata.setContentLength(file.getSize());
                metadata.setContentType(file.getContentType());

                s3Config.amazonS3().putObject(
                        new PutObjectRequest(bucket, uuidFileName, inputStream, metadata)
                                .withCannedAcl(CannedAccessControlList.PublicRead)
                );
                String url = s3Config.amazonS3().getUrl(bucket, uuidFileName).toString();
                dto.setOrgName(orgName);
                dto.setName(uuidFileName);
                dto.setSize(file.getSize());
                dto.setType(file.getContentType());
                dto.setPath(url);

                dtoList.add(dto);
            } catch (IOException e) {
                log.error("S3 업로드 중 IOException 발생: {}", e.getMessage(), e);
                throw new RuntimeException("S3 업로드 실패", e);
            }
        }
        return dtoList;
    }

    @Override
    public void s3Delete(List<FileStore> fileStoreList) {
        log.info("s3 파일 삭제");
        for (FileStore fileStore : fileStoreList) {
            s3Config.amazonS3().deleteObject(bucket, fileStore.getName());
        }
    }
}
