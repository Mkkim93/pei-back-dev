package kr.co.pei.pei_app.domain.repository.survey.jpa;

import kr.co.pei.pei_app.admin.application.dto.surveys.type.AdminFindTypeDTO;
import kr.co.pei.pei_app.admin.application.dto.surveys.type.AdminFindTypeRecentDTO;
import kr.co.pei.pei_app.domain.entity.survey.SurveyType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SurveyTypeJpaRepository extends JpaRepository<SurveyType, Long> {

    @Query("select st from SurveyType st where st.isDeleted = false and st.isPublic = false and st.hospital.id = :hospitalId")
    Page<SurveyType> findPages(Pageable pageable, @Param("hospitalId") Long hospitalId);

    @Query("select st from SurveyType st where st.isDeleted = false and st.isPublic = :isPublic and st.hospital.id = :hospitalId")
    List<SurveyType> findList(@Param("isPublic") boolean isPublic, @Param("hospitalId") Long hospitalId);

    @Query("select st from SurveyType st where st.isDeleted = false")
    List<SurveyType> findAllList();

    @Query("select st from SurveyType st where st.isDeleted = false and st.isPublic = true")
    Page<SurveyType> findAllPages(Pageable pageable);

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

    @Query("select st from SurveyType st where st.isDeleted = false and st.hospital.id = :hospitalId")
    Page<SurveyType> findRecentPage(Pageable pageable, Long hospitalId);

    @Query("select st from SurveyType st where st.isDeleted = false and st.isPublic = true")
    Page<SurveyType> findRecentPublicPage(Pageable pageable);
}
