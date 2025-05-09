package kr.co.pei.pei_app.domain.repository.file;

import kr.co.pei.pei_app.domain.entity.file.FileStore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface FileStoreRepository extends JpaRepository<FileStore, Long> {

    List<FileStore> findByBoardIdIn(List<Long> boardId);

    // 스케쥴러 쿼리
    @Transactional
    @Modifying
    @Query("delete from FileStore f where f.used = false")
    int deleteByUsedFalse();
}
