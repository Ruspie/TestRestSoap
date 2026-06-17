package org.example.testrestsoap.repository;

import org.example.testrestsoap.entity.jpa.PersonEntity;

public interface JpaPersonRepository {

    PersonEntity findById(Long id);
    void save(PersonEntity personEntity);
    void updateName(Long id, String name);
    void deleteById(Long id);

}
