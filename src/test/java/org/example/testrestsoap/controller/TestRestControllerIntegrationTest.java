package org.example.testrestsoap.controller;

import org.example.testrestsoap.dto.ErrorResponseDto;
import org.example.testrestsoap.dto.TestResponseDto;
import org.example.testrestsoap.service.TestService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TestRestControllerIntegrationTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    void getCounter_ReturnDataWithStatus200() throws Exception {
        ResponseEntity<TestResponseDto> response = testRestTemplate.getForEntity("/api/test/counter/1", TestResponseDto.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        TestResponseDto testResponseDto = response.getBody();
        assertNotNull(testResponseDto);
        assertEquals(1L, testResponseDto.getId());
        assertEquals("OK", testResponseDto.getMessage());
        assertEquals("test1", testResponseDto.getName());
        assertEquals(0, testResponseDto.getCurrentCounterValue().compareTo(BigDecimal.valueOf(111.0)));
    }

    @Test
    void getCounter_ReturnError() throws Exception {
        ResponseEntity<ErrorResponseDto> response = testRestTemplate.getForEntity("/api/test/counter/999", ErrorResponseDto.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        ErrorResponseDto errorResponseDto = response.getBody();
        assertNotNull(errorResponseDto);
        assertNotNull(errorResponseDto.getErrorTime());
        assertEquals("Counter not found with id: 999", errorResponseDto.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST.value(), errorResponseDto.getCode());

    }
}
