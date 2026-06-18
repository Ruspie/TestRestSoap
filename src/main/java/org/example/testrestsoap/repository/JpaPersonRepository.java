package org.example.testrestsoap.repository;

import org.example.testrestsoap.entity.jpa.PersonEntity;

public interface JpaPersonRepository {

    PersonEntity findById(Long id);
    void save(PersonEntity personEntity);
    void update(PersonEntity personEntity);
    void deleteById(Long id);

}
