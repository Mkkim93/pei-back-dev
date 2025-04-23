package kr.co.pei.pei_app.batch.board.repository;

import kr.co.pei.pei_app.domain.entity.file.FileStore;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@Transactional
@ActiveProfiles("test")
@SpringBootTest
class BatchFileRepositoryTest {

    @Autowired
    private BatchFileRepository batchFileRepository;

    @Test
    void test01() {
        List<FileStore> result = batchFileRepository.findAllUsedByFalse();
        for (FileStore fileStore : result) {
            System.out.println("fileStore.getId() = " + fileStore.getId());
        }
        assertThat(result).isEmpty();
    }
}