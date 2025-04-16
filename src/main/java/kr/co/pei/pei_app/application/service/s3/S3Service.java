package kr.co.pei.pei_app.application.service.s3;

import kr.co.pei.pei_app.application.dto.file.FileBoardDTO;
import kr.co.pei.pei_app.domain.entity.file.FileStore;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface S3Service {

    List<FileBoardDTO> fileUpload(List<MultipartFile> file) throws IOException;

    void s3Delete(List<FileStore> fileStoreList);
}
