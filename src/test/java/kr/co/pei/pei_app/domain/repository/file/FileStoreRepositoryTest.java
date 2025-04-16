package kr.co.pei.pei_app.domain.repository.file;

import kr.co.pei.pei_app.domain.entity.file.FileStore;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@Transactional
@ActiveProfiles("test")
@SpringBootTest
class FileStoreRepositoryTest {

    @Autowired
    private FileStoreRepository repository;

    @Test
    @DisplayName("게시글을 참조하는 파일 객체 조회")
    void findByFileWithBoardId() {
        // given
        Long findNotEmptyBoardId = 10L; // 존재하는 게시글
        Long findEmptyBoardId = 1L; // 존재하지 않는 게시글

        List<Long> boardIds = new ArrayList<>();
        boardIds.add(findEmptyBoardId);
        boardIds.add(findNotEmptyBoardId);

        // when
        List<FileStore> notEmpEntity = repository.findByBoardIdIn(boardIds);

        // then
        assertThat(notEmpEntity).isNotEmpty();
    }
}