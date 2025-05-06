package kr.co.pei.pei_app.common.application.service.surveys;

import jakarta.persistence.EntityNotFoundException;
import kr.co.pei.pei_app.domain.entity.hospital.Ward;
import kr.co.pei.pei_app.domain.entity.survey.SurveyDepart;
import kr.co.pei.pei_app.domain.repository.hospital.WardJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommonWardService {

    private final WardJpaRepository jpaRepository;

    // 소속 병원의 모든 병동 조회
    public List<Ward> findByHospitalIdByWardName(Long hospitalId) {
        return jpaRepository.findWardByHospitalId(hospitalId);
    }

    public Ward findByWardId(Long wardId) {
        return jpaRepository.findById(wardId).orElseThrow(() -> new EntityNotFoundException("해당 병동은 존재하지 않는 병동입니다."));
    }
}
