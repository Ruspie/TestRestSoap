package org.example.testrestsoap;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.FileWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class GenerateOpenApiDocsTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void generateOpenApiYaml() throws Exception {

        byte[] apiYamlBytes = mockMvc.perform(MockMvcRequestBuilders.get("/v3/api-docs.yaml"))
                .andReturn()
                .getResponse()
                .getContentAsByteArray();

        String apiYaml = new String(apiYamlBytes, StandardCharsets.UTF_8);

        String apiYamlPath = Paths.get(".").toAbsolutePath().normalize().toString() + "/src/main/resources/api-docs.yaml";

        try (FileWriter fileWriter = new FileWriter(apiYamlPath, StandardCharsets.UTF_8)) {
            fileWriter.write(apiYaml);
        }
    }

}
