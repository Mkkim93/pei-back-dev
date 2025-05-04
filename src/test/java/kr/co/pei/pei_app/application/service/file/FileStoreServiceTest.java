package kr.co.pei.pei_app.application.service.file;

import kr.co.pei.pei_app.admin.application.service.file.FileStoreService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@ActiveProfiles("test")
@Transactional
@SpringBootTest
class FileStoreServiceTest {

    @Autowired
    private FileStoreService service;

    @Test
    @DisplayName("파일 삭제")
    void deleteFiles() {
        List<Long> boardIds = new ArrayList<>();
        boardIds.add(10L);
        service.deleteFilesIfBoardHasFiles(boardIds);
    }
}