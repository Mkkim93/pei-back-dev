package kr.co.pei.pei_app.domain.repository.hospital;

import kr.co.pei.pei_app.domain.entity.hospital.Ward;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WardJpaRepository extends JpaRepository<Ward, Long> {

    @Query("select w from Ward w where w.hospital.id = :hospitalId")
    List<Ward> findWardByHospitalId(@Param("hospitalId") Long hospitalId);
}
