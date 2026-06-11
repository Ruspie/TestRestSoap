package org.example.testrestsoap.controller;

import org.example.testrestsoap.dto.TestIncrementRequestDto;
import org.example.testrestsoap.dto.TestResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("Интеграционное тестирование счетчиков (Без моков)")
class TestRestControllerWithStartServiceTest {

    @Autowired
    private TestRestTemplate restTemplate; // Настоящий HTTP-клиент для тестов

    @Test
    @DisplayName("Получение счетчика из реальной H2 базы данных")
    void getCounter_ShouldReturnDataFromDatabase() {
        // Выполняем реальный GET-запрос. По данным из data.sql под ID 2 лежит 'test2' со значением 222
        ResponseEntity<TestResponseDto> response = restTemplate.getForEntity(
            "/api/test/counter/2",
            TestResponseDto.class
        );

        // Проверяем HTTP статус ответа
        assertEquals(HttpStatus.OK, response.getStatusCode());

        // Проверяем тело ответа, пришедшее из базы данных H2
        TestResponseDto body = response.getBody();
        assertNotNull(body);
        assertEquals(2L, body.getId());
        assertEquals("test2", body.getName());
        assertEquals("OK", body.getMessage());
        // BigDecimal сравниваем через compareTo, чтобы избежать проблем со scale знаками после запятой
        assertEquals(0, BigDecimal.valueOf(222).compareTo(body.getCurrentCounterValue()));
    }

    @Test
    @DisplayName("Инкремент счетчика с физическим изменением строки в БД")
    void incrementCounter_ShouldModifyRowInDatabase() {
        // 1. Изначально на ID 1 в data.sql лежит значение 111.0.
        // Готовим запрос на прибавление 15.5
        TestIncrementRequestDto request = new TestIncrementRequestDto(1L, BigDecimal.valueOf(15.5));

        // 2. Отправляем настоящий POST-запрос с JSON телом
        ResponseEntity<TestResponseDto> postResponse = restTemplate.postForEntity(
            "/api/test/increment",
            request,
            TestResponseDto.class
        );

        assertEquals(HttpStatus.OK, postResponse.getStatusCode());
        TestResponseDto postBody = postResponse.getBody();
        assertNotNull(postBody);
        assertEquals(1L, postBody.getId());
        assertEquals("Incremented", postBody.getMessage());
        assertEquals(0, BigDecimal.valueOf(126.5).compareTo(postBody.getCurrentCounterValue()));

        // 3. ПРОВЕРКА: Делаем повторный независимый GET-запрос, чтобы убедиться,
        // что значение счетчика действительно перезаписалось в базе данных H2
        ResponseEntity<TestResponseDto> getResponse = restTemplate.getForEntity(
            "/api/test/counter/1",
            TestResponseDto.class
        );

        assertNotNull(getResponse.getBody());
        assertEquals(0, BigDecimal.valueOf(126.5).compareTo(getResponse.getBody().getCurrentCounterValue()));
    }

    @Test
    @DisplayName("Проверка обработки ошибок через GlobalExceptionHandler")
    void getCounter_ShouldReturn400_WhenCounterDoesNotExist() {
        // Запрашиваем ID 999, которого заведомо нет в базе данных H2
        ResponseEntity<String> response = restTemplate.getForEntity(
            "/api/test/counter/999",
            String.class
        );

        // Наш GlobalExceptionHandler должен перехватить ошибку и вернуть 400 Bad Request
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        String jsonResponse = response.getBody();
        assertNotNull(jsonResponse);
        // Проверяем, что в JSON-строке ответа содержатся нужные поля и текст нашей ошибки
        assertTrue(jsonResponse.contains("\"code\":400"));
        assertTrue(jsonResponse.contains("Counter not found with id: 999"));
        assertTrue(jsonResponse.contains("errorTime"));
    }

}
