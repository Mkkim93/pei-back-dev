package kr.co.pei.pei_app.application.service.survey;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.pei.pei_app.application.dto.surveys.survey.CreateSurveyDTO;
import kr.co.pei.pei_app.application.dto.surveys.survey.FindSurveyDTO;
import kr.co.pei.pei_app.domain.entity.hospital.Hospital;
import kr.co.pei.pei_app.domain.entity.survey.Survey;
import kr.co.pei.pei_app.domain.entity.survey.SurveyDepart;
import kr.co.pei.pei_app.domain.entity.survey.SurveyType;
import kr.co.pei.pei_app.domain.entity.survey.enums.CategoryType;
import kr.co.pei.pei_app.domain.entity.users.Users;
import kr.co.pei.pei_app.domain.repository.survey.jpa.SurveyJpaRepository;
import kr.co.pei.pei_app.domain.repository.survey.query.SurveyQueryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class SurveyService {

    private final SurveyQueryRepository queryRepository;
    private final SurveyJpaRepository jpaRepository;
    private final ObjectMapper mapper;

    // TODO json 내부 title 못꺼냄
    public void save(CreateSurveyDTO dto) {
        try {
            // 1. DTO 에서 JSON 문자열 변환
            String jsonData = mapper.writerWithDefaultPrettyPrinter()
                    .writeValueAsString(dto);
            log.info("json Parse: {}", jsonData);

            String title = dto.getTitle();

            title = title.replaceAll("[^a-zA-Z0-9가-힣_-]", "_");  // 한글, 영문, 숫자, -, _ 만 허용

            log.info("FileName(before clean): {}", title);

            // 2. 저장할 경로 설정
            // (현재 프로젝트의 /src/main/resources/static/survey/form/ 폴더에 저장하는 예시)
            Path path = Paths.get("src/main/resources/static/survey/form/" + title + ".json");

            // 3. 파일로 저장 (UTF-8 인코딩)
            Files.createDirectories(path.getParent()); // 폴더 없으면 자동 생성
            Files.write(path, jsonData.getBytes(StandardCharsets.UTF_8));

            log.info("JSON 파일 저장 완료: {}", path.toAbsolutePath());

            CategoryType categoryType = parseCategory(dto.getCategory());

            Survey survey = Survey.builder()
                    .title(title)
                    .category(String.valueOf(categoryType))
                    .content(jsonData)
                    .openAt(dto.getOpenAt())
                    .closeAt(dto.getCloseAt())
                    .type(new SurveyType(dto.getSurveyTypeId()))
                    .hospital(new Hospital(dto.getHospitalId()))
                    .depart(new SurveyDepart(dto.getSurveyDepartId()))
                    .users(new Users(dto.getUsersId()))
                    .build();

            jpaRepository.save(survey);

        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON 변환 실패", e);
        } catch (IOException e) {
            throw new RuntimeException("파일 저장 실패", e);
        }
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
    public Page<FindSurveyDTO> findSurveyMyPage(Pageable pageable, Long hospitalId) {
        return queryRepository.findMySurveyPage(pageable, hospitalId);
    }
}
