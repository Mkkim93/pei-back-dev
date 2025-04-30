package kr.co.pei.pei_app.application.service.hospital;

import kr.co.pei.pei_app.application.dto.hospital.FindHosDTO;
import kr.co.pei.pei_app.application.dto.hospital.UpdateHosDTO;
import kr.co.pei.pei_app.application.exception.hospital.HospitalExistException;
import kr.co.pei.pei_app.domain.entity.hospital.Hospital;
import kr.co.pei.pei_app.domain.entity.users.Users;
import kr.co.pei.pei_app.domain.repository.hospital.HospitalJpaRepository;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@Transactional
@ActiveProfiles("test")
class HospitalServiceTest {

    @Autowired
    private HospitalService service;

    @Autowired
    private HospitalJpaRepository jpaRepository;

    @BeforeEach
    void savedDefaultHospital() {

        Users mockUser = Users.builder()
                .id(1L)
                .username("user1")
                .name("관리자1")
                .password("1234")
                .build();
        Authentication authentication =
                new UsernamePasswordAuthenticationToken(mockUser, null, Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String hos1 = "병원1";
        String hos2 = "병원2";
        String hos3 = "병원3";

        Hospital h1 = new Hospital();
        Hospital h2 = new Hospital();
        Hospital h3 = new Hospital();

        h1.saveHospital(hos1);
        h2.saveHospital(hos2);
        h3.saveHospital(hos3);

        jpaRepository.save(h1);
        jpaRepository.save(h2);
        jpaRepository.save(h3);
    }

    @Test
    @DisplayName("저장 성공")
    void saveHospital() {

        // given
        String newHos1 = "병원4";

        // when
        service.saveHospital(newHos1);
        boolean existsByName = jpaRepository.existsByName(newHos1);

        // then
        assertThat(existsByName).isTrue();
    }

    @Test
    @DisplayName("중복된 병원명 저장 실패")
    void existHosNameSaveFailed() {

        // given
        String newHos2 = "병원1";

        // then (## 서비스 모듈 내부에서 예외 발생했을 때 테스트 방법)
        assertThatThrownBy(() -> service.saveHospital(newHos2))
                .isInstanceOf(HospitalExistException.class)
                .hasMessageContaining("이미 등록된 병원입니다.");
    }

    @Test
    @DisplayName("병원 리스트 조회 (페이징)")
    void findPages() {

        // given
        PageRequest pages = PageRequest.of(0, 10);
        Page<FindHosDTO> result = service.findPages(pages);

        // when
        List<FindHosDTO> content = result.getContent();
        Pageable pageable = result.getPageable();

        for (FindHosDTO dto : content) {
            System.out.println("Name: " + dto.getName());
        }

        // then
        assertThat(content.size()).isEqualTo(3);
        assertThat(pageable.getPageSize()).isEqualTo(10);
        assertThat(pageable.getPageNumber()).isEqualTo(0);
    }

    @Test
    @DisplayName("병원명 수정")
    void updateName() {

        // given
        UpdateHosDTO updateHosDTO = new UpdateHosDTO();
        updateHosDTO.setId(1L);
        updateHosDTO.setName("병원4");

        // when
        boolean updated = service.updateName(updateHosDTO);

        // then
        assertThat(updated).isTrue();
    }

    @Test
    @DisplayName("병원 데이터 삭제(논리 삭제)")
    void deletedHospital() {

        // given
        Long deletedId1 = 1L;

        // when
        boolean deleted = service.deletedHospital(deletedId1);

        // then
        assertThat(deleted).isTrue();
    }

    @Test
    @DisplayName("병원 데이터 삭제 실패(논리 삭제 실패)")
    void deletedHospitalFailed() {

        // given
        Long deletedId = 5L;

        // when, then
        assertThatThrownBy(() -> service.deletedHospital(deletedId))
                .isInstanceOf(HospitalExistException.class)
                .hasMessageContaining("병원 데이터 삭제가 실패 되었습니다.");
    }

    @Test
    @DisplayName("병원 데이터 복구 성공")
    void recoveredHospital() {

        // given
        Long recoverId = 1L;

        // when
        boolean result = service.recoveredHospital(recoverId);

        // then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("병원 데이터 복구 실패")
    void recoverHospitalFailed() {

        // given
        Long recoverId = 10L;

        // when, then
        Assertions.assertThatThrownBy(() -> service.recoveredHospital(recoverId))
                .isInstanceOf(HospitalExistException.class)
                .hasMessageContaining("병원 데이터 복구에 실패했습니다");
    }
}