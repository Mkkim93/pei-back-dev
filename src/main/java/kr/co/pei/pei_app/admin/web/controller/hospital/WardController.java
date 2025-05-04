package kr.co.pei.pei_app.admin.web.controller.hospital;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "WARD_API", description = "관리자가 병동 등록을 위한 API")
@RestController
@RequestMapping("/api/ward")
@RequiredArgsConstructor
public class WardController {
}
