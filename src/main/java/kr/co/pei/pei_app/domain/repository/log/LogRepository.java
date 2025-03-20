package kr.co.pei.pei_app.domain.repository.log;

import kr.co.pei.pei_app.domain.entity.log.Log;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LogRepository extends JpaRepository<Log, Long> {

    @Query("select l from Log l where l.users.id = :usersId and l.isDeleted = false")
    Page<Log> findByUsersId(@Param("usersId") Long usersId, Pageable pageable);

    @Modifying
    @Query("update Log l set l.isDeleted = true where l.id = :logId")
    int deleteLogId(@Param("logId") Long logId);
}
