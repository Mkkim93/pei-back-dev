package kr.co.pei.pei_app.admin.application.service.hospital;

import jakarta.persistence.EntityNotFoundException;
import kr.co.pei.pei_app.admin.application.dto.hospital.AdminFindHosDTO;
import kr.co.pei.pei_app.admin.application.dto.hospital.AdminUpdateHosDTO;
import kr.co.pei.pei_app.admin.application.exception.hospital.HospitalExistException;
import kr.co.pei.pei_app.admin.application.service.auth.UsersContextService;
import kr.co.pei.pei_app.domain.entity.hospital.Hospital;
import kr.co.pei.pei_app.domain.repository.hospital.HospitalJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class HospitalService {

    private final UsersContextService contextService;
    private final HospitalJpaRepository jpaRepository;

    // 내 병원 정보 조회
    public AdminFindHosDTO findMyHospitalInfo() {
        Long hospitalId = contextService.getCurrentUser().getHospital().getId();
        Hospital hospital = jpaRepository.findById(hospitalId).orElseThrow(() -> new EntityNotFoundException("해당 병원을 찾을 수 없습니디."));
        return new AdminFindHosDTO().toDto(hospital);
    }

    // 병원 등록
    public void saveHospital(String name) {
        boolean existsByName = jpaRepository.existsByName(name);
        if (existsByName) {
            throw new HospitalExistException(name + " 은 이미 등록된 병원입니다. 병원명을 다시 확인해주세요.");
        }
        Hospital hospital = new Hospital();
        hospital.saveHospital(name);
        jpaRepository.save(hospital);
    }

    // 병원 목록 조회 (페이징)
    public Page<AdminFindHosDTO> findPages(Pageable pageable) {
        Page<Hospital> pages = jpaRepository.findPages(pageable);
        return pages.map(hospital -> new AdminFindHosDTO(
                hospital.getId(),
                hospital.getName(),
                hospital.getDescription(),
                hospital.getImgUrl()
        ));
    }

    // 병원 수정
    public void updateName(AdminUpdateHosDTO adminUpdateHosDTO) {
        int updated = jpaRepository.updateName(adminUpdateHosDTO.getName(), adminUpdateHosDTO.getId());
        if (updated == 0) {
            throw new IllegalArgumentException(adminUpdateHosDTO.getId() + "의 업데이트에 실패 하였습니다.");
        }
    }

    // 병원 삭제 (논리 삭제)
    public void deletedHospital(Long id) {
        int deleted = jpaRepository.deletedHospitalId(id);

        if (deleted == 0) {
            throw new IllegalArgumentException(id + "의 병원 데이터 삭제가 실패 되었습니다.");
        }
    }

    // 병원 복구 (논리 복구)
    public void recoveredHospital(Long id) {
        int recovered = jpaRepository.recoverHospitalId(id);

        if (recovered == 0) {
            throw new IllegalArgumentException(id + "의 병원 데이터 복구에 실패했습니다.");
        }
    }
}
