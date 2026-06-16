package org.example.testrestsoap.service;

import org.example.testrestsoap.dto.TestResponseDto;
import org.example.testrestsoap.entity.TestEntity;
import org.example.testrestsoap.repository.TestRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class TestServiceTest {

    @Mock
    private TestRepository testRepository;

    @InjectMocks
    private TestService testService;

    @Test
    void getCounterById_Success() {
        TestEntity testEntity = new TestEntity(1L, "Mock", BigDecimal.TEN);
        Mockito.when(testRepository.findById(1L)).thenReturn(Optional.of(testEntity));

        TestResponseDto result = testService.getCounterById(1L);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(testEntity.getCounter(), result.getCurrentCounterValue());
        Assertions.assertEquals("OK", result.getMessage());
        Assertions.assertEquals(testEntity.getId(), result.getId());

        Mockito.verify(testRepository, Mockito.times(1)).findById(1L);
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
