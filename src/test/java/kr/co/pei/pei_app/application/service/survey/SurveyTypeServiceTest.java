package kr.co.pei.pei_app.application.service.survey;

import kr.co.pei.pei_app.admin.application.dto.surveys.type.AdminFindTypeDTO;
import kr.co.pei.pei_app.admin.application.dto.surveys.type.AdminUpdateTypeDTO;
import kr.co.pei.pei_app.admin.application.service.survey.SurveyTypeService;
import kr.co.pei.pei_app.domain.entity.survey.SurveyType;
import kr.co.pei.pei_app.domain.repository.survey.jpa.SurveyTypeJpaRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@Slf4j
@SpringBootTest
@ActiveProfiles("test")
class SurveyTypeServiceTest {

    @Autowired
    private SurveyTypeJpaRepository jpaRepository;

    @Autowired
    private SurveyTypeService service;

    private List<Long> savedIds;

    @BeforeEach
    void saveDefaultTypes() {
        savedIds = new ArrayList<>();
        List<String> saveName = List.of("대기 시간 만족도", "진료 예약 편의성");

        for (String name : saveName) {
            SurveyType type = SurveyType.builder()
                    .name(name)
                    .build();
            SurveyType saved = jpaRepository.save(type);
            savedIds.add(saved.getId()); // 저장된 id 수집
        }

    }

    @Test
    void findPages() {

        // given
        PageRequest page = PageRequest.of(0, 10);

        // when

        Page<AdminFindTypeDTO> result = service.findPages(page, null);

        List<AdminFindTypeDTO> content = result.getContent();
        Pageable pageable = result.getPageable();

        // then
        assertThat(pageable.getPageSize()).isEqualTo(page.getPageSize());
        assertThat(content).isNotEmpty();
        for (AdminFindTypeDTO dto : content) {
            System.out.println(dto.getName());
        }

    }

    @Test
    void saveType() {

        List<String> saveList = new ArrayList<>();
        saveList.add("추가 진료과1");
        saveList.add("추가 진료과2");

        service.saveType(saveList);

        List<SurveyType> all = jpaRepository.findAll();
        assertThat(all).extracting("name")
                .contains("추가 진료과1", "추가 진료과2");

    }
    @Test
    void recoveredTypes_shouldRecoverSuccessfully() {
        // given
        service.deletedTypes(savedIds); // 먼저 삭제
        List<SurveyType> deletedTypes = jpaRepository.findAllById(savedIds);
        for (SurveyType type : deletedTypes) {
            assertThat(type.getIsDeleted()).isTrue();
        }

        // when
        service.recoveredTypes(savedIds);

        // then
        List<SurveyType> recoveredTypes = jpaRepository.findAllById(savedIds);
        for (SurveyType type : recoveredTypes) {
            assertThat(type.getIsDeleted()).isFalse();
        }
    }

    @Test
    void deletedTypes() {

        service.deletedTypes(savedIds);

        List<SurveyType> deletedIds = jpaRepository.findAllById(savedIds);

        for (SurveyType deletedId : deletedIds) {
            assertThat(deletedId.getIsDeleted()).isTrue();
        }

    }

    @Test
    void recoveredTypes() {

        service.recoveredTypes(savedIds);

        List<SurveyType> recoveredIds = jpaRepository.findAllById(savedIds);

        for (SurveyType recoveredId : recoveredIds) {
            assertThat(recoveredId.getIsDeleted()).isFalse();
        }
    }

    @Test
    void updatedType() {
        AdminUpdateTypeDTO dto = new AdminUpdateTypeDTO();
        dto.setId(savedIds.get(0));
        dto.setName("설문지 이름 변경");

        service.updatedType(dto);
        SurveyType surveyType = jpaRepository.findById(dto.getId()).get();
        assertThat(surveyType.getName()).isEqualTo(dto.getName());
    }
}