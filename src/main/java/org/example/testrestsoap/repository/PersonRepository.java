package org.example.testrestsoap.repository;

import org.example.testrestsoap.entity.jpa.PersonEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<PersonEntity, Long>, PersonRepositoryCustom {

    @Query("FROM PersonEntity")
    List<PersonEntity> findAllHql();

    @Query("SELECT p.name, p.age FROM PersonEntity p")
    List<Object[]> findNamesAndAgesHql();

    @Query("FROM PersonEntity p WHERE p.name = :name AND p.age > :minAge")
    List<PersonEntity> findByNameAndAgeGreaterThanHql(@Param("name") String name, @Param("minAge") Integer minAge);

    @Query("FROM PersonEntity p ORDER BY p.age ASC, p.name DESC")
    List<PersonEntity> findAllOrderedHql();

    @Query("SELECT COUNT(p), AVG(p.age), SUM(p.age) FROM PersonEntity p")
    Object[] getAggregateStatsHql();

    @Query("SELECT p.primaryAddress.city, COUNT(p) FROM PersonEntity p GROUP BY p.primaryAddress.city HAVING AVG(p.age) > 18")
    List<Object[]> getCityStatsWithHavingHql();

    @Query("FROM PersonEntity p WHERE p.age > (SELECT AVG(sub.age) FROM PersonEntity sub)")
    List<PersonEntity> findOlderThanAverageHql();
}
