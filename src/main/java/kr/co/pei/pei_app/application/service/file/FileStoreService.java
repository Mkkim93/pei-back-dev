package kr.co.pei.pei_app.application.service.file;

import kr.co.pei.pei_app.domain.repository.file.FileStoreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileStoreService {

    private final FileStoreRepository fileStoreRepository;


}
