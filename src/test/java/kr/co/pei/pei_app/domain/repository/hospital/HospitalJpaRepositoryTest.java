package kr.co.pei.pei_app.domain.repository.hospital;

import kr.co.pei.pei_app.domain.entity.hospital.Hospital;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@Transactional
@ActiveProfiles("test")
class HospitalJpaRepositoryTest {

    @Autowired
    private HospitalJpaRepository jpaRepository;

    @BeforeEach
    void addHospital() {
        String name = "건양대병원";
        Hospital hospital = new Hospital();
        hospital.saveHospital(name);
        jpaRepository.save(hospital);
    }

    @Test
    void existsByName() {
        // given
        String newName = "충남대병원";
        String existName = "건양대병원";

        // when
        boolean exists1 = jpaRepository.existsByName(newName);
        boolean exists2 = jpaRepository.existsByName(existName);

        // then
        assertThat(exists1).isFalse();
        assertThat(exists2).isTrue();
    }
}