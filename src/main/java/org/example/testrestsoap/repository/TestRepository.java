package org.example.testrestsoap.repository;

import org.example.testrestsoap.entity.TestEntity;

import java.util.Optional;

public interface TestRepository {

    Optional<TestEntity> findById(Long id);
    TestEntity save(TestEntity test);

}
