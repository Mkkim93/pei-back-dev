package kr.co.pei.pei_app.admin.application.service.file;

import kr.co.pei.pei_app.domain.repository.file.FileStoreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
//@Component
@RequiredArgsConstructor
public class FileStoreScheduler {

    private final FileStoreRepository fileStoreRepository;

//    @Scheduled(cron = "0 0 0 * * *")
//    public void deleteUnusedFiles() {
//
//        int deletedCount = fileStoreRepository.deleteByUsedFalse();
//        log.info("used false file 삭제 수: {} ", deletedCount);
//        // TODO S3 버킷 데이터 어떻게 할지 고민
//
//    }

}
