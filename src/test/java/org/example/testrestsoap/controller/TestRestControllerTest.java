package org.example.testrestsoap.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.testrestsoap.dto.TestIncrementRequestDto;
import org.example.testrestsoap.dto.TestResponseDto;
import org.example.testrestsoap.service.TestService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TestRestController.class)
class TestRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TestService testService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getCounter_ShouldReturnMockedDataAndStatus200() throws Exception {
        TestResponseDto mockResponse = new TestResponseDto(1L, "OK", "Mock Counter", BigDecimal.valueOf(999.5));
        when(testService.getCounterById(1L)).thenReturn(mockResponse);

        mockMvc.perform(get("/api/test/counter/1"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.name").value("Mock Counter"))
            .andExpect(jsonPath("$.currentCounterValue").value(999.5))
            .andExpect(jsonPath("$.message").value("OK"));

        verify(testService, times(1)).getCounterById(1L);
    }

    @Test
    void incrementCounter_ShouldAcceptJsonAndReturnUpdatedMockValue() throws Exception {
        TestIncrementRequestDto requestDto = new TestIncrementRequestDto(1L, BigDecimal.valueOf(50));
        TestResponseDto responseDto = new TestResponseDto(1L, "Incremented", null, BigDecimal.valueOf(1049.5));

        when(testService.incrementCounter(any(TestIncrementRequestDto.class))).thenReturn(responseDto);

        mockMvc.perform(post("/api/test/increment")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestDto)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message").value("Incremented"))
            .andExpect(jsonPath("$.currentCounterValue").value(1049.5));

        verify(testService, times(1)).incrementCounter(any(TestIncrementRequestDto.class));
    }

    @Test
    void getCounter_ShouldPassExceptionToGlobalHandler_WhenServiceThrows() throws Exception {
        when(testService.getCounterById(999L)).thenThrow(new RuntimeException("Counter not found with id: 999"));

        mockMvc.perform(get("/api/test/counter/999"))
            .andExpect(status().isBadRequest())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.message").value("Counter not found with id: 999"))
            .andExpect(jsonPath("$.code").value(400))
            .andExpect(jsonPath("$.errorTime").exists());

        verify(testService, times(1)).getCounterById(999L);
    }
}