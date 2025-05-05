package kr.co.pei.pei_app.common.application.service.hospital;

import kr.co.pei.pei_app.admin.application.dto.hospital.AdminFindHosDTO;
import kr.co.pei.pei_app.common.application.dto.hospital.FindHospitalDTO;
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
public class CommonHospitalService {

    private final HospitalJpaRepository jpaRepository;

    public Page<FindHospitalDTO> findPages(Pageable pageable) {
        Page<Hospital> pages = jpaRepository.findPages(pageable);
        return pages.map(hospital -> new FindHospitalDTO(
                hospital.getId(),
                hospital.getName(),
                hospital.getDescription(),
                hospital.getImgUrl()
        ));
    }
}
