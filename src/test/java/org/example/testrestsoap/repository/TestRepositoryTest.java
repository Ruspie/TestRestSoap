package org.example.testrestsoap.repository;

import org.example.testrestsoap.entity.TestEntity;
import org.example.testrestsoap.repository.impl.TestRepositoryImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
public class TestRepositoryTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private TestRepository testRepository;

    @BeforeEach
    void setUp() {
        testRepository = new TestRepositoryImpl(jdbcTemplate);
    }

    @Test
    void findById_ReturnDataFromBdSuccessful() {
        Optional<TestEntity> testEntityOptional = testRepository.findById(1L);

        assertTrue(testEntityOptional.isPresent());
        assertEquals("test1", testEntityOptional.get().getName());
        assertEquals(0, testEntityOptional.get().getCounter().compareTo(BigDecimal.valueOf(111L)));
    }

    @Test
    void findById_ReturnEmptyDataFromBd() {
        Optional<TestEntity> testEntityOptional = testRepository.findById(999L);

        assertFalse(testEntityOptional.isPresent());
    }

}
