package org.example.testrestsoap.repository;

import org.example.testrestsoap.entity.jpa.PersonEntity;

import java.util.List;

public interface PersonRepositoryCustom {

    List<PersonEntity> findByMultiCriteria(String namePart, Integer minAge, Integer maxAge, String city);

    Long countTotalPersonsByCriteria();

}
