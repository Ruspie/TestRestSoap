package org.example.testrestsoap.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.testrestsoap.dto.TestIncrementRequestDto;
import org.example.testrestsoap.dto.TestResponseDto;
import org.example.testrestsoap.entity.TestEntity;
import org.example.testrestsoap.repository.TestRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class TestService {

    private final TestRepository testRepository;

    public Integer sum(Integer first, Integer second) {
        return first + second;
    }

    public TestResponseDto getCounterById(Long id) {
        try {
            log.info("Called getCounterById");

            Optional<TestEntity> testEntityOptional = testRepository.findById(id);

            TestEntity testEntity = testEntityOptional.get();

            TestResponseDto testRequestDto = new TestResponseDto();
            testRequestDto.setId(testEntity.getId());
            testRequestDto.setMessage("OK");
            testRequestDto.setName(testEntity.getName());
            testRequestDto.setCurrentCounterValue(testEntity.getCounter());

            return testRequestDto;
        } catch (Exception e) {
            throw new RuntimeException("Counter not found with id: " + id );
        }
    }

    public TestResponseDto incrementCounter(TestIncrementRequestDto testRequestDto) {
        log.info("Called incrementCounter");

        TestEntity testEntity = testRepository.findById(testRequestDto.getId()).orElseThrow(() -> {
            throw new RuntimeException("Data not found!");
        });

        testEntity.setCounter(testEntity.getCounter().add(testRequestDto.getIncrementForCounter()));

        testRepository.save(testEntity);

        TestResponseDto testResponseDto = new TestResponseDto();

        testResponseDto.setId(testEntity.getId());
        testResponseDto.setMessage("Incremented");
        testResponseDto.setCurrentCounterValue(testEntity.getCounter());

        return testResponseDto;
    }

}
