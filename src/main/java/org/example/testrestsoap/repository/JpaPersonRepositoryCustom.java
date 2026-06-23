package org.example.testrestsoap.repository;

import org.example.testrestsoap.entity.jpa.PersonEntity;

import java.util.List;

public interface JpaPersonRepositoryCustom {

    List<PersonEntity> findByMultiConditionsCriteria(String name, String namePart, Long minAge, Long maxAge, String passport, String city);

    Long countPersonByCriteria();

    List<PersonEntity> findAllWithOffsetAndLimit(Integer offset, Integer limit);

    int updatePersonAge(Long id, Long age);

    int updatePersonAgeHql(Long id, Long age);

}
