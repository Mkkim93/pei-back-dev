package kr.co.pei.pei_app.domain.repository.survey.jpa;

import kr.co.pei.pei_app.admin.application.dto.surveys.type.AdminUpdateTypeDTO;
import kr.co.pei.pei_app.domain.entity.survey.SurveyType;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@ActiveProfiles("test")
@SpringBootTest
class SurveyTypeJpaRepositoryTest {

    @Autowired
    private SurveyTypeJpaRepository jpaRepository;

    private List<Long> savedIds;

    @BeforeEach
    void defaultTypeSave() {
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
    @DisplayName("유형 조회 (페이징)")
    void findPages() {
        PageRequest page = PageRequest.of(0, 20);
        Page<SurveyType> pages = jpaRepository.findPages(page, null);

        List<SurveyType> content = pages.getContent();
        for (SurveyType surveyType : content) {
            System.out.println("types: " + surveyType.toString());
        }
    }

    @Test
    @DisplayName("유형 삭제 (논리 삭제)")
    void deletedTypeIds() {
        // given
        // when
        int deleted = jpaRepository.deleteTypeIds(savedIds);

        // then
        Assertions.assertThat(deleted).isEqualTo(savedIds.size());
    }

    @Test
    @DisplayName("유형 복구 (is_deleted -> false)")
    void recoveredTypeIds() {

        int recovered = jpaRepository.recoverTypeIds(savedIds);

        Assertions.assertThat(recovered).isEqualTo(savedIds.size());
    }

    @Test
    @DisplayName("설문 유형 이름 수정")
    void updateName() {
        AdminUpdateTypeDTO dto = new AdminUpdateTypeDTO(savedIds.get(0), "수정이름");

        int updated = jpaRepository.updateName(dto.getName(), dto.getId());
        SurveyType updatedData = jpaRepository.findById(dto.getId()).get();

        Assertions.assertThat(updated).isEqualTo(1);
        Assertions.assertThat(updatedData.getName()).isEqualTo(dto.getName());

    }
}