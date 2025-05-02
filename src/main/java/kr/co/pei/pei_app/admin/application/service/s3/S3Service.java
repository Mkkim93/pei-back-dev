package kr.co.pei.pei_app.admin.application.service.s3;

import kr.co.pei.pei_app.admin.application.dto.file.AdminFileBoardDTO;
import kr.co.pei.pei_app.domain.entity.file.FileStore;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface S3Service {

    List<AdminFileBoardDTO> fileUpload(List<MultipartFile> file) throws IOException;

    void s3Delete(List<FileStore> fileStoreList);
}
