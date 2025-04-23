package kr.co.pei.pei_app.batch.board.reader;

import kr.co.pei.pei_app.batch.board.repository.BatchFileRepository;
import kr.co.pei.pei_app.domain.entity.file.FileStore;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.support.IteratorItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.List;

@Component
@RequiredArgsConstructor
public class UnusedFileReader {

    private final BatchFileRepository batchFileRepository;


//    @Bean
//    public ItemReader<FileStore> unusedFileReader() {
//        List<FileStore> fileList = batchFileRepository.findAllUsedByFalse();
//        return new IteratorItemReader<>(fileList);
//    }
}
