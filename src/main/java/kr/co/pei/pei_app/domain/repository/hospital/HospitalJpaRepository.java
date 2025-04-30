package kr.co.pei.pei_app.domain.repository.hospital;

import kr.co.pei.pei_app.domain.entity.hospital.Hospital;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface HospitalJpaRepository extends JpaRepository<Hospital, Long> {

    @Query("select h from Hospital h where h.isDeleted = false")
    Page<Hospital> findPages(Pageable pageable);

    boolean existsByName(String name);

    @Modifying
    @Query("update Hospital h set h.name = :updateName where h.id = :id")
    int updateName(@Param("updateName") String updateName, @Param("id") Long id);

    @Modifying
    @Query("update Hospital h set h.isDeleted = true where h.id = :id")
    int deletedHospitalId(@Param("id") Long id);

    @Modifying
    @Query("update Hospital h set h.isDeleted = false where h.id = :id")
    int recoverHospitalId(@Param("id") Long id);

}
