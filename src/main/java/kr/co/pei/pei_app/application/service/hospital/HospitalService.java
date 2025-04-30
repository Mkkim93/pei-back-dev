package kr.co.pei.pei_app.application.service.hospital;

import kr.co.pei.pei_app.application.dto.hospital.FindHosDTO;
import kr.co.pei.pei_app.application.dto.hospital.UpdateHosDTO;
import kr.co.pei.pei_app.application.exception.hospital.HospitalExistException;
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

    private final HospitalJpaRepository jpaRepository;

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

    // 병원 조회
    public Page<FindHosDTO> findPages(Pageable pageable) {
        Page<Hospital> pages = jpaRepository.findPages(pageable);
        return pages.map(hospital -> new FindHosDTO(
                hospital.getId(),
                hospital.getName()
        ));
    }

    // 병원 수정
    public boolean updateName(UpdateHosDTO updateHosDTO) {
        int updated = jpaRepository.updateName(updateHosDTO.getName(), updateHosDTO.getId());
        if (updated == 0) {
            throw new IllegalArgumentException(updateHosDTO.getId() + "의 업데이트에 실패 하였습니다.");
        }
        return true;
    }

    // 병원 삭제 (논리 삭제)
    public boolean deletedHospital(Long id) {
        int deleted = jpaRepository.deletedHospitalId(id);

        if (deleted == 0) {
            throw new IllegalArgumentException(id + "의 병원 데이터 삭제가 실패 되었습니다.");
        }
        return true;
    }

    // 병원 복구 (논리 복구)
    public boolean recoveredHospital(Long id) {
        int recovered = jpaRepository.recoverHospitalId(id);

        if (recovered == 0) {
            throw new IllegalArgumentException(id + "의 병원 데이터 복구에 실패했습니다.");
        }
        return true;
    }
}
