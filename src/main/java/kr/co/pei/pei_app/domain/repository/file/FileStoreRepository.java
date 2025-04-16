package kr.co.pei.pei_app.domain.repository.file;

import kr.co.pei.pei_app.domain.entity.file.FileStore;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileStoreRepository extends JpaRepository<FileStore, Long> {

    List<FileStore> findByBoardIdIn(List<Long> boardId);
}
