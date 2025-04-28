package kr.co.pei.pei_app.domain.repository.survey.jpa;

import kr.co.pei.pei_app.domain.entity.survey.SurveyDepart;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface SurveyDepartJpaRepository extends JpaRepository<SurveyDepart, Long> {

    @Query("select sd from SurveyDepart sd where sd.isDeleted = false")
    Page<SurveyDepart> findPages(Pageable pageable);

    List<SurveyDepart> findByNameIn(List<String> names);

    @Modifying
    @Query("update SurveyDepart sb set sb.isDeleted = true where sb.id in :ids")
    int deleteDepartIds(@Param("ids") List<Long> ids);

    @Modifying
    @Query("update SurveyDepart sb set sb.isDeleted = false where sb.id in :ids")
    int recoverDepartIds(@Param("ids") List<Long> ids);

    @Modifying
    @Query("update SurveyDepart sb set sb.name = :updateName where sb.id = :id")
    int updateName(@Param("updateName") String updateName, @Param("id") Long id);
}
