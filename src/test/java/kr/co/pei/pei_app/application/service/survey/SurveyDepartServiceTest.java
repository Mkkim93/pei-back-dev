package kr.co.pei.pei_app.application.service.survey;

import kr.co.pei.pei_app.admin.application.service.survey.SurveyDepartService;
import kr.co.pei.pei_app.admin.application.dto.surveys.depart.AdminFindDepartDTO;
import kr.co.pei.pei_app.admin.application.dto.surveys.depart.AdminUpdateDepartDTO;
import kr.co.pei.pei_app.domain.entity.survey.SurveyDepart;
import kr.co.pei.pei_app.domain.repository.survey.jpa.SurveyDepartJpaRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@Slf4j
@ActiveProfiles("test")
@SpringBootTest
class SurveyDepartServiceTest {

    @Autowired
    private SurveyDepartService surveyDepartService;

    @Autowired
    private SurveyDepartJpaRepository surveyDepartJpaRepository;

    @BeforeEach
    void addSampleDepart() {

        SurveyDepart depart = new SurveyDepart("가정의학과");
        surveyDepartJpaRepository.save(depart);
    }

    @Test
    @DisplayName("진료과 등록 테스트")
    void saveDeparts_ForExcel_엑셀업로드_성공() throws Exception {
        // given: 테스트용 엑셀 파일 생성
        MockMultipartFile file = createTestExcelFile();

        // when: 서비스 메서드 호출
        surveyDepartService.saveDepartsForExcel(file);

        // then: DB에 데이터가 들어갔는지 확인
        List<SurveyDepart> departList = surveyDepartJpaRepository.findAll();

        assertThat(departList).isNotEmpty();
        assertThat(departList).extracting("name")
                .containsExactlyInAnyOrder("가정의학과", "내과(감염)", "내과(내분비)");
    }

    // 테스트용 엑셀파일 생성 메서드
    private MockMultipartFile createTestExcelFile() throws Exception {
        try (XSSFWorkbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("Sheet1");

            // 첫 번째 행은 헤더니까 실제 데이터는 두 번째 행부터 시작하는 식으로!
            Row row1 = sheet.createRow(0);
            row1.createCell(0).setCellValue(1);  // id
            row1.createCell(1).setCellValue("가정의학과"); // name

            Row row2 = sheet.createRow(1);
            row2.createCell(0).setCellValue(2);
            row2.createCell(1).setCellValue("내과(감염)");

            Row row3 = sheet.createRow(2);
            row3.createCell(0).setCellValue(3);
            row3.createCell(1).setCellValue("내과(내분비)");

            workbook.write(out);

            return new MockMultipartFile(
                    "file",           // name (form-data field name)
                    "depart_test.xlsx", // original filename
                    "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                    out.toByteArray()
            );
        }
    }

    @Test
    @DisplayName("진료과 조회 (페이징)")
    void findPages() {
        // given
        PageRequest page = PageRequest.of(0, 20);

        // when
        Page<AdminFindDepartDTO> result = surveyDepartService.findPages(page);

        // then
        List<AdminFindDepartDTO> content = result.getContent();

        for (AdminFindDepartDTO dto : content) {
            System.out.println("dto.getName: " + dto.getName());
        }
    }

    @Test
    @DisplayName("진료과 추가 등록")
    void addDepart() {
        // given
        List<String> departNames = new ArrayList<>();
        departNames.add("치과");
        departNames.add("내과");

        // when
        surveyDepartService.saveDepart(departNames);

        // then
        List<SurveyDepart> departList = surveyDepartJpaRepository.findAll();
        for (SurveyDepart depart : departList) {
            System.out.println(depart);
        }

        assertThat(departList).isNotEmpty();
        assertThat(departList).extracting("name")
                .containsExactlyInAnyOrder("가정의학과", "치과", "내과");
    }

    @Test
    @DisplayName("진료과 이름 수정 (리스트)")
    void updateNames() {
        // given
        AdminUpdateDepartDTO dto = new AdminUpdateDepartDTO();
        dto.setId(22L);
        dto.setName("정형외과");

        // when
        surveyDepartService.updateDeparts(dto);

        // then
        SurveyDepart depart = surveyDepartJpaRepository.findById(22L).get();

        Assertions.assertThat(depart.getName()).isEqualTo(dto.getName());
    }
}