package org.example.testrestsoap.repository.impl;

import org.example.testrestsoap.entity.TestEntity;
import org.example.testrestsoap.repository.TestRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class TestRepositoryImpl implements TestRepository {

    private final JdbcTemplate jdbcTemplate;

    public TestRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Маппер для преобразования строк БД в Java-объект
    private final RowMapper<TestEntity> testEntityRowMapper = (rs, rowNum) -> new TestEntity(
        rs.getLong("id"),
        rs.getString("name"),
        rs.getBigDecimal("counter")
    );

    @Override
    public Optional<TestEntity> findById(Long id) {
        // Использование FOR UPDATE блокирует строку для текущего потока, решая проблему Race Condition
        String sql = "SELECT id, name, counter FROM test_counters WHERE id = ? FOR UPDATE";
        try {
            TestEntity entity = jdbcTemplate.queryForObject(sql, testEntityRowMapper, id);
            return Optional.ofNullable(entity);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public TestEntity save(TestEntity entity) {
        if (entity.getId() == null) {
            // Логика Insert (если нужно добавлять новые записи)
            String sql = "INSERT INTO test_counters (name, counter) VALUES (?, ?)";
            jdbcTemplate.update(sql, entity.getName(), entity.getCounter());
            return entity;
        } else {
            // Логика Update для существующих записей
            String sql = "UPDATE test_counters SET name = ?, counter = ? WHERE id = ?";
            jdbcTemplate.update(sql, entity.getName(), entity.getCounter(), entity.getId());
            return entity;
        }
    }
}