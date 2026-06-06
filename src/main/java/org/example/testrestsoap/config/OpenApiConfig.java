package org.example.testrestsoap.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(info = @Info(
        title = "TestRestSoap API",
        description = "Документация для проекта TestRestSoap",
        contact = @Contact(
                name = "Ruspie",
                url = "https://github.com/Ruspie/TestRestSoap",
                email = "begun4ik2@gmail.com"
        ),
        summary = "TestRestSoap API Documentation",
        version = "1.0.0"
))
@Configuration
public class OpenApiConfig {

    /*@Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("TestRestSoap API")
                        .version("1.0")
                        .description("Документация для проекта TestRestSoap")
                        .contact(new Contact()
                                .name("Ruspie")
                                .url("https://github.com/Ruspie/TestRestSoap")));
    }*/

}
