package kr.co.pei.pei_app.admin.application.service.hospital;

import jakarta.validation.constraints.NotNull;
import kr.co.pei.pei_app.admin.application.exception.hospital.SurveyWardException;
import kr.co.pei.pei_app.admin.application.service.auth.UsersContextService;
import kr.co.pei.pei_app.domain.entity.hospital.Hospital;
import kr.co.pei.pei_app.domain.entity.hospital.Ward;
import kr.co.pei.pei_app.domain.repository.hospital.WardJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class WardService {

    private final WardJpaRepository jpaRepository;
    private final UsersContextService contextService;
    private final HospitalService hospitalService;

    public void saveWardForExcel(MultipartFile file) {

        Hospital hospital = contextService.getCurrentUser().getHospital();
//        Hospital hospital = hospitalService.findById(hospitalId);

        if (!file.getOriginalFilename().endsWith(".xlsx")) {
            throw new SurveyWardException("업로드 파일은 .xlsx 확장자만 허용 가능합니다.");
        }

        List<Ward> wardList = new ArrayList<>();

        try (XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream())) {
            XSSFSheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                if (row.getRowNum() == 0) {
                    continue;
                }
                Cell idCell = row.getCell(0);
                Cell nameCell = row.getCell(1);

                Ward ward = getWard(idCell, nameCell, hospital);
                wardList.add(ward);
            }
            jpaRepository.saveAll(wardList);
        } catch (IOException e) {
            log.info("엑셀 업로드 실패: {}", e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    @NotNull
    private static Ward getWard(Cell idCell, Cell nameCell, Hospital hospital) {
        if (idCell == null || nameCell == null) {
            throw new SurveyWardException("엑셀 파일에 id 또는 이름이 누락되었습니다.");
        }

        long id = (long) idCell.getNumericCellValue();
        String name = getCellStringValue(nameCell);

        if (name.trim().isEmpty()) {
            throw new SurveyWardException("병동 이름이 비어있습니다.");
        }

        Ward ward = new Ward();
        ward.setWardInfo(id, name, hospital);

        return ward;
    }

    private static String getCellStringValue(Cell cell) {
        if (cell == null) return "";

        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue();
            case NUMERIC -> String.valueOf((long) cell.getNumericCellValue()); // 정수로 변환
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            case FORMULA -> cell.getCellFormula();
            case BLANK -> "";
            default -> "";
        };
    }
}
