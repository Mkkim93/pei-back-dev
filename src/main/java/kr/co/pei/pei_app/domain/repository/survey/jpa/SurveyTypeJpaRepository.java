package kr.co.pei.pei_app.domain.repository.survey.jpa;

import kr.co.pei.pei_app.domain.entity.survey.SurveyType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface SurveyTypeJpaRepository extends JpaRepository<SurveyType, Long> {

    @Query("select st from SurveyType st where st.isDeleted = false")
    Page<SurveyType> findPages(Pageable pageable);

    List<SurveyType> findByNameIn(List<String> names);

    @Modifying
    @Query("update SurveyType st set st.isDeleted = true where st.id in :ids")
    int deleteTypeIds(@Param("ids") List<Long> ids);

    @Modifying
    @Query("update SurveyType st set st.isDeleted = false where st.id in :ids")
    int recoverTypeIds(@Param("ids") List<Long> ids);


    @Modifying
    @Query("update SurveyType st set st.name = :updateName where st.id = :id")
    int updateName(@Param("updateName") String updateName, @Param("id") Long id);
}
