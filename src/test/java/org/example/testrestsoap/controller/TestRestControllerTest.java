package org.example.testrestsoap.controller;

import io.swagger.v3.oas.annotations.media.Schema;
import org.example.testrestsoap.dto.ErrorResponseDto;
import org.example.testrestsoap.dto.TestResponseDto;
import org.example.testrestsoap.service.TestService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TestRestController.class)
public class TestRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TestService testService;

    @Test
    void getCounter_ReturnDataWithStatus200() throws Exception {
        TestResponseDto mockResponse = new TestResponseDto(1L, "OK", "Mock data", BigDecimal.ONE);
        when(testService.getCounterById(anyLong())).thenReturn(mockResponse);

        mockMvc.perform(get("/api/test/counter/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.name").value("Mock data"))
                .andExpect(jsonPath("$.currentCounterValue").value(1));

        verify(testService, times(1)).getCounterById(1L);
    }

    @Test
    void getCounter_ReturnError() throws Exception {
        when(testService.getCounterById(anyLong())).thenThrow(new RuntimeException("Some error"));

        mockMvc.perform(get("/api/test/counter/1"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errorTime").exists())
                .andExpect(jsonPath("$.code").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value("Some error"));

        verify(testService, times(1)).getCounterById(1L);
    }
}
