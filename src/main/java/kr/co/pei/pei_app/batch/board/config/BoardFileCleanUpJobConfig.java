package kr.co.pei.pei_app.batch.board.config;

import kr.co.pei.pei_app.batch.board.reader.UnusedFileReader;
import kr.co.pei.pei_app.domain.entity.file.FileStore;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class BoardFileCleanUpJobConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;

    private final UnusedFileReader unusedFileReader;
    // TODO S3Delete 의존성 추가

    @Bean
    public Job boardFileCleanupJob() {
        return new JobBuilder("boardFileCleanupJob", jobRepository)
                .start(deleteUnusedFileStep())
                .build();
    }

    @Bean
    public Step deleteUnusedFileStep() {
//        return new StepBuilder("deleteUnusedFileStep", jobRepository)
//                .<FileStore, FileStore> chunk(100, platformTransactionManager)
//                .reader(unusedFileReader.unusedFileReader())
//                .writer()
//                .build();
        return null;
    }
}
