package kr.co.pei.pei_app.application.service.survey;

import kr.co.pei.pei_app.application.dto.survey.depart.FindDepartDTO;
import kr.co.pei.pei_app.application.dto.survey.depart.UpdateDepartDTO;
import kr.co.pei.pei_app.application.exception.survey.SurveyDepartException;
import kr.co.pei.pei_app.domain.entity.survey.SurveyDepart;
import kr.co.pei.pei_app.domain.repository.survey.jpa.SurveyDepartJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class SurveyDepartService {

    private final SurveyDepartJpaRepository jpaRepository;

    // 초기 데이터 진료과(엑셀) 등록
    public void saveDepartsForExcel(MultipartFile file) throws IOException {

        if (!file.getOriginalFilename().endsWith(".xlsx")) {
            throw new SurveyDepartException("업로드 파일은 .xlsx 확장자만 허용됩니다.");
        }

        List<SurveyDepart> departList = new ArrayList<>();

        try (XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream())){

            XSSFSheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                if (row.getRowNum() == 0) {
                    continue;
                }
                    Cell idCell = row.getCell(0);
                    Cell nameCell = row.getCell(1);

                SurveyDepart depart = getSurveyDepart(idCell, nameCell);
                departList.add(depart);
                }
            jpaRepository.saveAll(departList);
        } catch (IllegalArgumentException e) {
            log.info("엑셀 업로드 실패: {}",e.getMessage(), e);
            throw new IllegalArgumentException("엑셀 업로드 실패", e);
        }
    }

    @NotNull
    private static SurveyDepart getSurveyDepart(Cell idCell, Cell nameCell) {
        if (idCell == null || nameCell == null) {
            throw new SurveyDepartException("엑셀 파일에 id 또는 name 이 누락된 행이 있습니다.");
        }

        Long id = (long) idCell.getNumericCellValue();
        String name = nameCell.getStringCellValue();

        if (name.trim().isEmpty()) {
            throw new SurveyDepartException("진료과 이름이 비어 있습니다.");
        }

        SurveyDepart depart = new SurveyDepart();
        depart.setDepartIdAndName(id, name);
        return depart;
    }

    // 진료과 추가 등록
    public void saveDepart(List<String> names) {
        List<SurveyDepart> existingDeparts = jpaRepository.findByNameIn(names);

        if (!existingDeparts.isEmpty()) {
            List<String> existingNames = existingDeparts.stream()
                    .map(SurveyDepart::getName)
                    .toList();

            throw new SurveyDepartException("이미 존재하는 진료과가 있습니다: " + existingNames);
        }

        List<SurveyDepart> departs = new ArrayList<>();

        for (String name : names) {
            SurveyDepart build = SurveyDepart.builder()
                    .name(name)
                    .build();
            departs.add(build);
        }

        jpaRepository.saveAll(departs);
    }

    // 진료과 조회 (삭제된 진료과 제외)
    public Page<FindDepartDTO> findPages(Pageable pageable) {
        Page<SurveyDepart> entity = jpaRepository.findPages(pageable);
        return entity.map(depart -> new FindDepartDTO(
                depart.getId(),
                depart.getName()
        ));
    }

    public boolean deletedDeparts(List<Long> ids) {
        int deleted = jpaRepository.deleteDepartIds(ids);
        return deleted == ids.size();
    }

    public boolean recoverDeparts(List<Long> ids) {
        int recovered = jpaRepository.recoverDepartIds(ids);
        return recovered == ids.size();
    }

    public void updateDeparts(UpdateDepartDTO updateDepartDTO) {

        boolean exists = jpaRepository.existsById(updateDepartDTO.getId());

        if (!exists) {
            throw new SurveyDepartException("해당 진료과는 존재하지 않습니다.");
        }

        int updated = jpaRepository.updateName(updateDepartDTO.getName(), updateDepartDTO.getId());

        if (updated == 0) {
            throw new SurveyDepartException("진료과 수정에 실패 했습니다.");
        }
    }
}
