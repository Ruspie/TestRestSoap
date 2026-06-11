package org.example.testrestsoap.repository;

import org.example.testrestsoap.entity.TestEntity;
import org.example.testrestsoap.repository.impl.TestRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@JdbcTest // Тестирует только JDBC-слой в изолированной H2 базе
class TestRepositoryTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private TestRepository testRepository;

    @BeforeEach
    void setUp() {
        testRepository = new TestRepositoryImpl(jdbcTemplate);
    }

    @Test
    void findById_ShouldReturnDataFromDataSql() {
        // Проверяем, что запись, вставленная через data.sql, успешно находится
        Optional<TestEntity> entity = testRepository.findById(1L);

        assertTrue(entity.isPresent());
        assertEquals("test1", entity.get().getName());
        // H2 может вернуть scale в зависимости от настроек, сравниваем через compareTo
        assertEquals(0, BigDecimal.valueOf(111).compareTo(entity.get().getCounter()));
    }

    @Test
    void findById_ShouldReturnEmpty_WhenIdDoesNotExist() {
        Optional<TestEntity> entity = testRepository.findById(999L);
        assertFalse(entity.isPresent());
    }

    @Test
    void save_ShouldUpdateRowInDatabase() {
        TestEntity updatedEntity = new TestEntity(1L, "New Name Log", BigDecimal.valueOf(555.5));
        testRepository.save(updatedEntity);

        // Извлекаем напрямую, проверяя физическое обновление в БД
        Optional<TestEntity> databaseRecord = testRepository.findById(1L);
        assertTrue(databaseRecord.isPresent());
        assertEquals("New Name Log", databaseRecord.get().getName());
        assertEquals(0, BigDecimal.valueOf(555.5).compareTo(databaseRecord.get().getCounter()));
    }
}
