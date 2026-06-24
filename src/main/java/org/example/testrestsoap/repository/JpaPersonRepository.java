package org.example.testrestsoap.repository;

import org.example.testrestsoap.entity.jpa.PersonEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface JpaPersonRepository extends JpaRepository<PersonEntity, Long>, JpaPersonRepositoryCustom, JpaPersonRepositoryQueryDsl {

    @Query("FROM PersonEntity")
    List<PersonEntity> findAllHql();

    @Query("SELECT p.name, p.age FROM PersonEntity p")
    List<Object[]> findNamesAndAgesHql();

    @Query("SELECT p FROM PersonEntity p WHERE p.name = :name AND p.age > :minAge")
    List<PersonEntity> findByNameAndAgeGreaterThanHql(@Param("name") String name, @Param("minAge") Long minAge);

    @Query("FROM PersonEntity p ORDER BY p.age ASC, p.name DESC")
    List<PersonEntity> findAllOrderedHql();

    @Query("SELECT 'COUNT', count(p), 'AVG', avg(p.age), 'SUM', sum(p.age), 'MIN', min(p.age) FROM PersonEntity p")
    Object[] getAggregatedInfoHql();

    @Query("SELECT p.primaryAddress.city, count(p) FROM PersonEntity p GROUP BY p.primaryAddress.city")
    List<Object[]> getCityStatHql();

    @Query("SELECT p FROM PersonEntity p WHERE (p.age > (SELECT avg(p1.age) FROM PersonEntity p1))")
    List<PersonEntity> findAllOlderThanAverageAgeHql();

    @Query("SELECT p FROM PersonEntity p JOIN FETCH p.passport WHERE p.passport.passportNumber = :passportNumber")
    List<PersonEntity> findByPassportNumberHql(@Param("passportNumber") String passportNumber);

    @Query("SELECT p FROM PersonEntity p JOIN FETCH p.passport WHERE p.passport.passportNumber = :passportNumber")
    Optional<PersonEntity> findByPassportNumberOptionalHql(@Param("passportNumber") String passportNumber);

    /// /////////////

    List<PersonEntity> findByName(String name);

    List<PersonEntity> findByAgeBetween(Long minAge, Long maxAge);

    List<PersonEntity> findByNameContainingIgnoreCase(String namePart);

    List<PersonEntity> findByPrimaryAddressCity(String city);

    boolean existsByPassportPassportNumber(String passportNumber);

    @Query(value = "SELECT * FROM persons WHERE name LIKE CONCAT('%', :namePart, '%')", nativeQuery = true)
    List<PersonEntity> findByNameNativeSQL(@Param("namePart") String namePart);

    @Query(value = """
        SELECT p.* FROM persons p
        JOIN person_companies pc ON p.id = pc.person_id
        JOIN companies c ON pc.company_id = c.id
        WHERE c.title = :companyTitle
        """, nativeQuery = true)
    List<PersonEntity> findWorkersByCompanyNameNativeSQL(@Param("companyTitle") String companyTitle);

}
