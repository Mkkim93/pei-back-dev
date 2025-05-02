package kr.co.pei.pei_app.admin.application.service.survey;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import kr.co.pei.pei_app.admin.application.dto.surveys.survey.*;
import kr.co.pei.pei_app.domain.entity.hospital.Hospital;
import kr.co.pei.pei_app.domain.entity.survey.Survey;
import kr.co.pei.pei_app.domain.entity.survey.SurveyDepart;
import kr.co.pei.pei_app.domain.entity.survey.SurveyType;
import kr.co.pei.pei_app.domain.entity.survey.enums.CategoryType;
import kr.co.pei.pei_app.domain.entity.survey.enums.SurveyStatus;
import kr.co.pei.pei_app.domain.entity.users.Users;
import kr.co.pei.pei_app.domain.repository.survey.jpa.SurveyJpaRepository;
import kr.co.pei.pei_app.domain.repository.survey.query.SurveyQueryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class SurveyService {

    private final SurveyQueryRepository queryRepository;
    private final SurveyJpaRepository jpaRepository;
    private final ObjectMapper mapper;

    public SurveyService(SurveyQueryRepository queryRepository,
                         SurveyJpaRepository jpaRepository, ObjectMapper mapper) {
        this.queryRepository = queryRepository;
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    // TODO json 내부 title 못꺼냄
    public void save(AdminCreateSurveyDTO dto) {
        try {
            // 1. DTO 에서 JSON 문자열 변환
            String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(dto.getContent());

            log.info("json Parse: {}", json);

            SurveyStatus surveyStatus = surveyStatusCheck(dto); // 설문 상태 지정 (오늘 날짜보다 앞이면 ACTIVE, 뒤면 WAITING)

            String title = dto.getTitle();
            CategoryType categoryType = parseCategory(dto.getCategory());

            Survey survey = Survey.builder()
                    .title(title)
                    .category(categoryType)
                    .content(json)
                    .openAt(dto.getOpenAt())
                    .closeAt(dto.getCloseAt())
                    .surveyStatus(surveyStatus)
                    .isVisible(dto.isVisible())
                    .type(new SurveyType(dto.getSurveyTypeId()))
                    .hospital(new Hospital(dto.getHospitalId()))
                    .depart(new SurveyDepart(dto.getSurveyDepartId()))
                    .users(new Users(dto.getUsersId()))
                    .build();

            jpaRepository.save(survey);

        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON 변환 실패", e);
        }
    }

    private SurveyStatus surveyStatusCheck(AdminSurveyCheckTimeSupport dto) {

        LocalDateTime today = LocalDateTime.now();
        LocalDateTime openAt = dto.getOpenAt();

        String surveyStatus = "";
        if (today.isBefore(openAt)) {
            surveyStatus = "WAITING";
        }
        if (today.isAfter(openAt)) {
            surveyStatus = "ACTIVE";
        }
        return SurveyStatus.valueOf(surveyStatus);
    }

    private CategoryType parseCategory(String cateText) {
        CategoryType[] values = CategoryType.values();
        for (CategoryType value : values) {
            if (value.getText().equals(cateText)) {
                return value;
            }
        }
        throw new IllegalArgumentException("Unknown category text: " + cateText);
    }

    public Map<CategoryType, String> findCategory() {
        CategoryType[] values = CategoryType.values();
        Map<CategoryType, String> text = new HashMap<>();
        for (CategoryType value : values) {
            text.put(value, value.getText());
        }
        return text;
    }

    @Transactional(readOnly = true)
    public Page<AdminFindSurveyDTO> findSurveyMyPage(Pageable pageable, Long hospitalId) {
        return queryRepository.findMySurveyPage(pageable, hospitalId);
    }

    public AdminSurveyDetailDTO findSurveyDetail(Long id, Long hospitalId) {
        return queryRepository.findSurveyDetail(id, hospitalId);
    }

    public void update(AdminUpdateSurveyDTO dto, SurveyType surveyType, SurveyDepart surveyDepart) throws JsonProcessingException {
        Survey survey = jpaRepository.findById(dto.getId()).orElseThrow(() -> new EntityNotFoundException("설문 양식 조회 오류"));
        String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(dto.getContent());
        CategoryType categoryType = parseCategory(dto.getCategory());
        SurveyStatus surveyStatus = surveyStatusCheck(dto);
        survey.updateForm(dto, categoryType, json, surveyType, surveyDepart, surveyStatus);
    }
}
