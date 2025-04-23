package kr.co.pei.pei_app.domain.repository.file;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static kr.co.pei.pei_app.domain.entity.file.QFileStore.*;

@Repository
@RequiredArgsConstructor
public class FileQueryRepository {

    private final JPAQueryFactory queryFactory;

    public void orgFileUpdate(List<Long> fileId) {
        queryFactory.update(fileStore)
                .set(fileStore.used, false)
                .where(fileStore.id.in(fileId))
                .execute();
    }
}
