package kr.co.pei.pei_app.common.web.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "USER_HOSPITAL_API", description = "일반 사용자가 병원 선택을 하기 위한 API")
@RequestMapping("/api/com-hospital")
@RestController
@RequiredArgsConstructor
public class CommonHospitalController {

    // TODO 모든 사용자가 각 설문에 참여 할 병원 정보 조회
}
