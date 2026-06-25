package org.example.testrestsoap.repository;

import com.querydsl.core.Tuple;
import org.example.testrestsoap.entity.jpa.PassportEntity;
import org.example.testrestsoap.entity.jpa.PersonEntity;

import java.util.List;
import java.util.Optional;

public interface JpaPersonRepositoryQueryDsl {

    List<PersonEntity> findByMultiConditionsQueryDsl(String name, String namePart, Long minAge, Long maxAge, String passport, String city);

    List<PersonEntity> findPersonsWithPaginationQueryDsl(int offset, int limit);

    List<Tuple> getCityWithStatsByMinAverageAgeQueryDsl(Long minAverageAge);

    Optional<PersonEntity> findPersonByPassportNumberQueryDsl(String passportNumber);

    List<String> findUniqueCitiesQueryDsl();

}
