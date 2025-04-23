package kr.co.pei.pei_app.batch.board.writer;

import kr.co.pei.pei_app.batch.board.repository.BatchFileRepository;
import kr.co.pei.pei_app.config.s3.S3Config;
import kr.co.pei.pei_app.domain.entity.file.FileStore;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class S3DeleteWriter {

    private final S3Config s3Config;
    private final BatchFileRepository batchFileRepository;

//    @Bean
//    public ItemWriter<FileStore> writer() {
//       s3Config.amazonS3().deleteObject();
//    }

}
