package kr.co.pei.pei_app.application.service.survey;

import kr.co.pei.pei_app.application.dto.surveys.type.FindTypeDTO;
import kr.co.pei.pei_app.application.dto.surveys.type.UpdateTypeDTO;
import kr.co.pei.pei_app.application.exception.surveys.SurveyTypeException;
import kr.co.pei.pei_app.domain.entity.survey.SurveyType;
import kr.co.pei.pei_app.domain.repository.survey.jpa.SurveyTypeJpaRepository;
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

    // 유형 조회 (페이징)
    public Page<FindTypeDTO> findPages(Pageable pageable) {
        Page<SurveyType> entity = jpaRepository.findPages(pageable);
        return entity.map(type -> new FindTypeDTO(
                type.getId(),
                type.getName()
        ));
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
    public boolean deletedTypes(List<Long> ids) {
        int deleted = jpaRepository.deleteTypeIds(ids);
        if (deleted != ids.size()) {
            throw new SurveyTypeException("삭제 대상 수와 실제 삭제된 수가 일치하지 않습니다.");
        }
        return true;
    }

    // 삭제된 유형 복구
    public boolean recoveredTypes(List<Long> ids) {
        int recovered = jpaRepository.recoverTypeIds(ids);
        if (recovered != ids.size()) {
            throw new SurveyTypeException("복구 대상 수와 실제 복수된 수가 일치하지 않습니다.");
        }
        return true;
    }

    // 유형 수정
    public void updatedType(UpdateTypeDTO updateTypeDTO) {

        int updated = jpaRepository.updateName(updateTypeDTO.getName(), updateTypeDTO.getId());

        if (updated == 0) {
            throw new SurveyTypeException("ID " + updateTypeDTO.getId() + "에 설문 유형이 존재하지 않거나 이름 수정에 실패 하였습니다.");
        }
    }
}
