package kr.co.pei.pei_app.domain.repository.log;

import kr.co.pei.pei_app.domain.entity.log.Log;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogRepository extends JpaRepository<Log, Long> {
}
