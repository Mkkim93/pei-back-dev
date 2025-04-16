package kr.co.pei.pei_app.application.service.s3;

import com.amazonaws.services.s3.model.CannedAccessControlList;
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
            String orgName = file.getOriginalFilename();

            if (orgName == null || !orgName.contains(".")) {
                throw new IllegalArgumentException("파일명이 유효하지 않습니다.");
            }

            String ext = orgName.substring(orgName.lastIndexOf("."));
            String uuidFileName = UUID.randomUUID() + ext;
            String localPath = localLocation + uuidFileName;

            File localFile = new File(localPath);
            file.transferTo(localFile);
            log.info("로컬 파일 저장 완료: {}", localPath);

            s3Config.amazonS3().putObject(
                    new PutObjectRequest(bucket, uuidFileName, localFile)
                            .withCannedAcl(CannedAccessControlList.PublicRead)
            );

            String url = s3Config.amazonS3().getUrl(bucket, uuidFileName).toString();

            dto.setOrgName(orgName);
            dto.setName(uuidFileName);
            dto.setSize(file.getSize());
            dto.setType(file.getContentType());
            dto.setPath(url);

            dtoList.add(dto);

            if (localFile.delete()) {
                log.info("로컬 파일 삭제 완료: {}", localPath);
            } else {
                log.warn("로컬 파일 삭제 실패: {}", localPath);
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
