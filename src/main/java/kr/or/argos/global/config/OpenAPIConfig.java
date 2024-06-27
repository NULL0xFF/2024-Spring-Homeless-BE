package kr.or.argos.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityScheme.Type;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

  @Bean
  public OpenAPI openAPI() {
    Info info = new Info()
        .version("1.0.0")
        .title("ARGOS Homeless Project")
        .description("API Specification")
        .contact(new Contact().name("ARGOS").email("webmaster@argos.or.kr")
            .url("https://github.com/4RG0S"));
    String bearerAuth = "bearerAuth";
    Components components = new Components()
        .addSecuritySchemes(bearerAuth, new SecurityScheme()
            .name(bearerAuth)
            .type(Type.HTTP)
            .scheme("bearer")
            .bearerFormat("JWT"));
    return new OpenAPI()
        .info(info)
        .components(components);
  }
}