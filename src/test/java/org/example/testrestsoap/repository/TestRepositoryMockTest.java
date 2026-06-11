package org.example.testrestsoap.repository;

import org.example.testrestsoap.entity.TestEntity;
import org.example.testrestsoap.repository.impl.TestRepositoryImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class) // Подключаем поддержку Mockito в JUnit 5
@DisplayName("Unit-тестирование репозитория через Mockito (Без БД)")
public class TestRepositoryMockTest {

    @Mock
    private JdbcTemplate jdbcTemplate; // Создаем мок для спрингового JdbcTemplate

    @InjectMocks
    private TestRepositoryImpl testRepository; // Mockito автоматически внедрит мок jdbcTemplate сюда

    @Test
    @DisplayName("Успешный поиск счетчика по ID")
    @SuppressWarnings("unchecked")
    void findById_Success() {
        // 1. Готовим тестовую сущность, которую якобы вернет база данных
        TestEntity mockEntity = new TestEntity(1L, "Test DB Counter", BigDecimal.valueOf(100));

        // Мокаем вызов queryForObject. Нам важно, чтобы при любых параметрах и мапперах возвращался наш mockEntity
        when(jdbcTemplate.queryForObject(any(String.class), any(RowMapper.class), eq(1L)))
            .thenReturn(mockEntity);

        // 2. Вызываем метод репозитория
        Optional<TestEntity> result = testRepository.findById(1L);

        // 3. Проверяем результаты
        assertTrue(result.isPresent());
        assertEquals("Test DB Counter", result.get().getName());
        assertEquals(BigDecimal.valueOf(100), result.get().getCounter());

        // Проверяем, что репозиторий действительно один раз вызвал jdbcTemplate с нужным ID
        verify(jdbcTemplate, times(1)).queryForObject(any(String.class), any(RowMapper.class), eq(1L));
    }

    @Test
    @DisplayName("Поиск по ID вернул пустоту (запись не найдена в БД)")
    @SuppressWarnings("unchecked")
    void findById_NotFound_ShouldReturnEmptyOptional() {
        // Спринговый JdbcTemplate при отсутствии строк выбрасывает EmptyResultDataAccessException
        when(jdbcTemplate.queryForObject(any(String.class), any(RowMapper.class), eq(999L)))
            .thenThrow(new EmptyResultDataAccessException(1));

        // Вызываем метод и проверяем, что репозиторий аккуратно поймал ошибку и вернул Optional.empty()
        Optional<TestEntity> result = testRepository.findById(999L);

        assertFalse(result.isPresent());
        verify(jdbcTemplate, times(1)).queryForObject(any(String.class), any(RowMapper.class), eq(999L));
    }

    @Test
    @DisplayName("Обновление (Save) существующей записи в БД")
    void save_ExistingEntity_ShouldExecuteUpdateSql() {
        // Готовим сущность с уже существующим ID (логика UPDATE)
        TestEntity existingEntity = new TestEntity(1L, "Update Name", BigDecimal.valueOf(120));

        // Метод update у JdbcTemplate возвращает количество измененных строк (например, 1)
        when(jdbcTemplate.update(any(String.class), eq("Update Name"), eq(BigDecimal.valueOf(120)), eq(1L)))
            .thenReturn(1);

        // Вызываем сохранение
        TestEntity result = testRepository.save(existingEntity);

        // Проверяем, что репозиторий вернул тот же объект и дернул нужный метод SQL
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(jdbcTemplate, times(1))
            .update(any(String.class), eq("Update Name"), eq(BigDecimal.valueOf(120)), eq(1L));
    }

    @Test
    @DisplayName("Вставка (Save) новой записи в БД")
    void save_NewEntity_ShouldExecuteInsertSql() {
        // Сущность без ID (логика INSERT)
        TestEntity newEntity = new TestEntity(null, "New Counter", BigDecimal.valueOf(50));

        when(jdbcTemplate.update(any(String.class), eq("New Counter"), eq(BigDecimal.valueOf(50))))
            .thenReturn(1);

        TestEntity result = testRepository.save(newEntity);

        assertNotNull(result);
        assertNull(result.getId()); // ID генерирует сама СУБД, поэтому в Unit-тесте он остается null
        verify(jdbcTemplate, times(1))
            .update(any(String.class), eq("New Counter"), eq(BigDecimal.valueOf(50)));
    }

}
