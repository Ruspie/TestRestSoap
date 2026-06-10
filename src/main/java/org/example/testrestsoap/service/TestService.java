package org.example.testrestsoap.service;

import org.example.testrestsoap.dto.TestIncrementRequestDto;
import org.example.testrestsoap.dto.TestResponseDto;
import org.example.testrestsoap.entity.TestEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TestService {

    private final Logger logger = LoggerFactory.getLogger(TestService.class);

    private Map<Long, TestEntity> storageMap = new ConcurrentHashMap<>();

    public TestService() {
        storageMap.put(1L, new TestEntity(1L, "test1", BigDecimal.valueOf(111)));
        storageMap.put(2L, new TestEntity(2L, "test2", BigDecimal.valueOf(222)));

        logger.info("INIT TestService");
    }

    public TestResponseDto getCounterById(Long id) {
        try {
            TestEntity testEntity = storageMap.get(id);

            TestResponseDto testRequestDto = new TestResponseDto();
            testRequestDto.setId(testEntity.getId());
            testRequestDto.setMessage("OK");
            testRequestDto.setName(testEntity.getName());
            testRequestDto.setCurrentCounterValue(testEntity.getCounter());

            return testRequestDto;
        } catch (Exception e) {
            throw new RuntimeException("Counter not found");
        }
    }

    public TestResponseDto incrementCounter(TestIncrementRequestDto testRequestDto) {
        if (!storageMap.containsKey(testRequestDto.getId()))
            throw new RuntimeException("Data not found!");

        TestEntity testEntity = storageMap.get(testRequestDto.getId());
        testEntity.setCounter(testEntity.getCounter().add(testRequestDto.getIncrementForCounter()));

        TestResponseDto testResponseDto = new TestResponseDto();

        testResponseDto.setId(testEntity.getId());
        testResponseDto.setMessage("Incremented");
        testResponseDto.setCurrentCounterValue(testEntity.getCounter());

        return  testResponseDto;
    }

}
