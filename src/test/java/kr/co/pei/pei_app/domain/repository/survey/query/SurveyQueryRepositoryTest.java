package kr.co.pei.pei_app.domain.repository.survey.query;

import kr.co.pei.pei_app.admin.application.dto.surveys.survey.AdminFindSurveyDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@Transactional
@ActiveProfiles("test")
class SurveyQueryRepositoryTest {

    @Autowired
    private SurveyQueryRepository queryRepository;

    @Test
    void findMySurveyPage() {
        PageRequest pages = PageRequest.of(0, 10);
        Long hospitalId = 26L;
        Page<AdminFindSurveyDTO> result = queryRepository.findMySurveyPage(pages, hospitalId);

        List<AdminFindSurveyDTO> content = result.getContent();
        for (AdminFindSurveyDTO dto : content) {
            System.out.println(dto.getTitle());
        }
    }
}