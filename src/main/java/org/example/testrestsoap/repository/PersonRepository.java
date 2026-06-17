package org.example.testrestsoap.repository;

import org.example.testrestsoap.entity.jpa.PersonEntity;

import java.util.List;

public interface PersonRepository {

    PersonEntity findById(Long id);

    void save(PersonEntity person);

    void updateName(Long id, String newName);

    void deleteById(Long id);

    List<PersonEntity> findByNameJpql(String name);

    int bulkDeleteCompaniesWithoutWorkers();

}
