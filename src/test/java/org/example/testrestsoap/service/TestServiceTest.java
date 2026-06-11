package org.example.testrestsoap.service;

import org.example.testrestsoap.dto.TestIncrementRequestDto;
import org.example.testrestsoap.dto.TestResponseDto;
import org.example.testrestsoap.entity.TestEntity;
import org.example.testrestsoap.repository.TestRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TestServiceTest {

    @Mock
    private TestRepository testRepository;

    @InjectMocks
    private TestService testService;

    @Test
    void getCounterById_Success() {
        TestEntity entity = new TestEntity(1L, "Mock Counter", BigDecimal.valueOf(100));
        when(testRepository.findById(1L)).thenReturn(Optional.of(entity));

        TestResponseDto result = testService.getCounterById(1L);

        assertNotNull(result);
        assertEquals(BigDecimal.valueOf(100), result.getCurrentCounterValue());
        assertEquals("OK", result.getMessage());
        verify(testRepository, times(1)).findById(1L);
    }

    @Test
    void getCounterById_ThrowsException_WhenNotFound() {
        when(testRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                                                  () -> testService.getCounterById(1L));

        assertTrue(exception.getMessage().contains("Counter not found"));
    }

    @Test
    void incrementCounter_Success() {
        TestEntity initialEntity = new TestEntity(1L, "Counter", BigDecimal.valueOf(10));
        TestIncrementRequestDto request = new TestIncrementRequestDto(1L, BigDecimal.valueOf(5));

        when(testRepository.findById(1L)).thenReturn(Optional.of(initialEntity));
        // Настраиваем mock, чтобы он возвращал то, что в него передают на сохранение
        when(testRepository.save(any(TestEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        TestResponseDto response = testService.incrementCounter(request);

        assertNotNull(response);
        assertEquals(BigDecimal.valueOf(15), response.getCurrentCounterValue());
        assertEquals("Incremented", response.getMessage());
    }
}
