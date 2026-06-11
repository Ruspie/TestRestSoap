package org.example.testrestsoap.service;

import lombok.RequiredArgsConstructor;
import org.example.testrestsoap.dto.TestIncrementRequestDto;
import org.example.testrestsoap.dto.TestResponseDto;
import org.example.testrestsoap.entity.TestEntity;
import org.example.testrestsoap.repository.TestRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TestService {

    private final TestRepository testRepository;

    public TestResponseDto getCounterById(Long id) {
        try {
            TestEntity testEntity = testRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Counter not found with id: " + id));

            TestResponseDto testRequestDto = new TestResponseDto();
            testRequestDto.setId(testEntity.getId());
            testRequestDto.setMessage("OK");
            testRequestDto.setName(testEntity.getName());
            testRequestDto.setCurrentCounterValue(testEntity.getCounter());

            return testRequestDto;
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            throw e;
        }
    }

    public TestResponseDto incrementCounter(TestIncrementRequestDto testRequestDto) {
        // Находим текущее состояние
        TestEntity testEntity = testRepository.findById(testRequestDto.getId())
            .orElseThrow(() -> new RuntimeException("Data not found!"));

        // Рассчитываем новое значение счетчика
        testEntity.setCounter(testEntity.getCounter().add(testRequestDto.getIncrementForCounter()));

        // Сохраняем обратно в репозиторий
        testEntity = testRepository.save(testEntity);

        TestResponseDto testResponseDto = new TestResponseDto();

        testResponseDto.setId(testEntity.getId());
        testResponseDto.setMessage("Incremented");
        testResponseDto.setCurrentCounterValue(testEntity.getCounter());

        return testResponseDto;
    }

}
