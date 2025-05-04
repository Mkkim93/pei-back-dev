package kr.co.pei.pei_app.admin.application.service.survey;

import jakarta.persistence.EntityNotFoundException;
import kr.co.pei.pei_app.admin.application.dto.surveys.type.*;
import kr.co.pei.pei_app.admin.application.exception.surveys.SurveyTypeException;
import kr.co.pei.pei_app.domain.entity.survey.SurveyType;
import kr.co.pei.pei_app.domain.repository.survey.jpa.SurveyTypeJpaRepository;
import kr.co.pei.pei_app.domain.repository.survey.query.SurveyTypeQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class SurveyTypeService {

    private final SurveyTypeJpaRepository jpaRepository;
    private final SurveyTypeQueryRepository queryRepository;

    public SurveyType findById(Long id) {
        return jpaRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("해당 유형은 존재하지 않는 유형입니다."));
    }

    // 유형 조회 (페이징) = 나의 병원에 등록된 설문 양식만 조회
    public Page<AdminFindTypeDTO> findPages(Pageable pageable, Long hospitalId) {
        Page<SurveyType> entity = jpaRepository.findPages(pageable, hospitalId);
        return entity.map(type -> new AdminFindTypeDTO(
                type.getId(),
                type.getName(),
                type.getDescription()
        ));
    }

    public List<AdminFindTypeDTO> findList(boolean isPublic, Long hospitalId) {

        if (isPublic) {
            List<SurveyType> allList = jpaRepository.findAllList();
            return allList.stream().map(type -> new AdminFindTypeDTO(
                    type.getId(),
                    type.getName(),
                    type.getDescription())).toList();
        }

        List<SurveyType> list = jpaRepository.findList(false, hospitalId);
        return list.stream().map(type -> new AdminFindTypeDTO(
                type.getId(),
                type.getName(),
                type.getDescription())).toList();
    }

    public Page<AdminTypeUsageDTO> findUsage(Pageable pageable, Long hospitalId, boolean isPublic) {
        return queryRepository.findUsagePage(pageable, hospitalId, isPublic);
    }

    public Page<AdminTypeStatusDTO> findStatusPages(Pageable pageable, Long hospitalId, boolean isPublic, String surveyStatus) {
        return queryRepository.findStatusPage(pageable, hospitalId, isPublic, surveyStatus);
    }


    // 유형 등록
    public void saveType(List<String> names) {
        List<SurveyType> existingTypes = jpaRepository.findByNameIn(names);

        if (!existingTypes.isEmpty()) {
            List<String> existingNames = existingTypes.stream()
                    .map(SurveyType::getName)
                    .toList();
            throw new SurveyTypeException("이미 동일한 이름의 설문유형이 존재합니다." + existingNames);
        }

        List<SurveyType> types = new ArrayList<>();
        for (String name : names) {

            SurveyType build = SurveyType.builder()
                    .name(name)
                    .build();
            types.add(build);
        }
        jpaRepository.saveAll(types);
    }

    // 유형 삭제
    public void deletedTypes(List<Long> ids) {
        int deleted = jpaRepository.deleteTypeIds(ids);
        if (deleted != ids.size()) {
            throw new SurveyTypeException("삭제 대상 수와 실제 삭제된 수가 일치하지 않습니다.");
        }
    }

    // 삭제된 유형 복구
    public void recoveredTypes(List<Long> ids) {
        int recovered = jpaRepository.recoverTypeIds(ids);
        if (recovered != ids.size()) {
            throw new SurveyTypeException("복구 대상 수와 실제 복수된 수가 일치하지 않습니다.");
        }
    }

    // 유형 수정
    public void updatedType(AdminUpdateTypeDTO adminUpdateTypeDTO) {

        int updated = jpaRepository.updateName(adminUpdateTypeDTO.getName(), adminUpdateTypeDTO.getId());

        if (updated == 0) {
            throw new SurveyTypeException("ID " + adminUpdateTypeDTO.getId() + "에 설문 유형이 존재하지 않거나 이름 수정에 실패 하였습니다.");
        }
    }

    public Page<AdminFindTypeDTO> findAllPages(Pageable pageable) {
        Page<SurveyType> pages = jpaRepository.findAllPages(pageable);
        return pages.map(type -> new AdminFindTypeDTO(
                type.getId(),
                type.getName(),
                type.getDescription()
        ));
    }

    public Page<AdminFindTypeRecentDTO> findRecent(Pageable pageable, Long hospitalId, boolean isPublic) {
        if (isPublic) {
            Page<SurveyType> pages = jpaRepository.findRecentPublicPage(pageable);
            return pages.map(type -> new AdminFindTypeRecentDTO(
                    type.getId(),
                    type.getName(),
                    type.getUpdatedAt()
            ));
        }

        Page<SurveyType> pages = jpaRepository.findRecentPage(pageable, hospitalId);

        return pages.map(type -> new AdminFindTypeRecentDTO(
                type.getId(),
                type.getName(),
                type.getUpdatedAt()
        ));
    }
}
