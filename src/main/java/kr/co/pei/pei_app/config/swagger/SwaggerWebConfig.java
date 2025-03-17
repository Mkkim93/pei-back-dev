package kr.co.pei.pei_app.config.swagger;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerWebConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .openapi("3.0.0")
                .components(new Components()
                        .addSecuritySchemes("Authorization", new SecurityScheme()
                                .type(SecurityScheme.Type.APIKEY)
                                .in(SecurityScheme.In.HEADER)
                                .name("Authorization")
                                .description("JWT Authorization header using the Bearer scheme")))
                .addSecurityItem(new SecurityRequirement().addList("Authorization"))
                .info(apiInfo());
    }

    private Info apiInfo() {
        return new Info()
                .title("PEI_APP REST API Specifications")
                .description("Specification")
                .version("1.0.0");
    }
}
