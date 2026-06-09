package org.example.testrestsoap.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(info = @Info(
        title = "TestRestApi",
        description = "Документация к проекту TestRestSoap",
        contact = @Contact(
                name = "Alex Begun",
                url = "https://github.com/Ruspie/TestRestSoap.git",
                email = "testEmail@gmail.com"
        ),
        summary = "TestRestSoap API",
        version = "1.0.0"
))
@Configuration
public class OpenApiConfig {

    /*@Bean
    public OpenAPI openAPI() {
        return new OpenAPI().
                info(new io.swagger.v3.oas.models.info.Info()
                        .title("TestRestApi")
                        .description("Документация к проекту TestRestSoap")
                        .contact(new io.swagger.v3.oas.models.info.Contact()
                                .name("Alex Begun")
                                .email("testEmail@gmail.com")
                                .url("https://github.com/Ruspie/TestRestSoap.git")
                        )
                        .summary("TestRestSoap API")
                        .version("1.0.0")
                );
    }*/

}
