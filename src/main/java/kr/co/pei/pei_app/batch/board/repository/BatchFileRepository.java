package kr.co.pei.pei_app.batch.board.repository;

import kr.co.pei.pei_app.domain.entity.file.FileStore;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class BatchFileRepository {

    private final JdbcTemplate jdbcTemplate;

    /*
     * 사용되지 않는 모든 튜블 조회 (used : false)
     */
    public List<FileStore> findAllUsedByFalse() {
        String sql = "select * from file_store where used = false";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(FileStore.class));
    }

    // TODO 사용되지 않는 컬럼을 어떻게 처리할 지 협의
}
