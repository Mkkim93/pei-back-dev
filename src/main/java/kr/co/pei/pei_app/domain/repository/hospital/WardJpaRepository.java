package kr.co.pei.pei_app.domain.repository.hospital;

import kr.co.pei.pei_app.domain.entity.hospital.Ward;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WardJpaRepository extends JpaRepository<Ward, Long> {
}
