//package org.example.testrestsoap;
//
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//
//import java.io.FileWriter;
//import java.nio.charset.StandardCharsets;
//import java.nio.file.Paths;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//@ActiveProfiles("test")
//class GenerateOpenApiDocsTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Test
//    void generateOpenApiYaml() throws Exception {
//        // Делаем виртуальный запрос к эндпоинту swagger для получения YAML спецификации
//        byte[] yamlBytes = mockMvc.perform(MockMvcRequestBuilders.get("/v3/api-docs.yaml"))
//                .andReturn()
//                .getResponse()
//                .getContentAsByteArray();
//
//        String yamlContent = new String(yamlBytes, StandardCharsets.UTF_8);
//
//        // Задаем путь сохранения в корень проекта или в ресурсы
//        String outputPath = Paths.get(".").toAbsolutePath().normalize().toString() + "/src/main/resources/api-docs.yaml";
//
//        // Записываем контент в файл
//        try (FileWriter fileWriter = new FileWriter(outputPath, StandardCharsets.UTF_8)) {
//            fileWriter.write(yamlContent);
//        }
//
//        System.out.println("=== SWAGGER YAML УСПЕШНО СГЕНЕРИРОВАН ПО ПУТИ: " + outputPath + " ===");
//    }
//}
