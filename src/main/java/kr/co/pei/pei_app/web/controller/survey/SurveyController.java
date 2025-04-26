package kr.co.pei.pei_app.web.controller.survey;


import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.Resource;

import java.io.*;
import java.nio.charset.StandardCharsets;


@Tag(name = "SURVEY_API", description = "설문지 응답 관련 API")
@RestController
@RequestMapping("/api/survey")
public class SurveyController {

    private final ApplicationContext applicationContext;

    public SurveyController(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @GetMapping
    public ResponseEntity<Object> getTest() throws IOException {
        Resource resource = applicationContext.getResource("classpath:static/survey/form/test.json");
        String content = new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);

        ObjectMapper objectMapper = new ObjectMapper();
        Object jsonObject = objectMapper.readValue(content, Object.class);

        return ResponseEntity.ok(jsonObject);
    }
}