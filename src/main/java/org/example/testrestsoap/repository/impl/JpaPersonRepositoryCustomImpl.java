package org.example.testrestsoap.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.example.testrestsoap.entity.jpa.AddressEntity;
import org.example.testrestsoap.entity.jpa.PassportEntity;
import org.example.testrestsoap.entity.jpa.PersonEntity;
import org.example.testrestsoap.repository.JpaPersonRepositoryCustom;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class JpaPersonRepositoryCustomImpl implements JpaPersonRepositoryCustom {

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public List<PersonEntity> findByMultiConditionsCriteria(String name, String namePart, Long minAge, Long maxAge, String passport, String city) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<PersonEntity> query = criteriaBuilder.createQuery(PersonEntity.class);
        Root<PersonEntity> personRoot = query.from(PersonEntity.class);

        List<Predicate> predicates = new ArrayList<>();

        if (name != null && !name.isEmpty())
            predicates.add(criteriaBuilder.equal(personRoot.get("name"), name));

        if (namePart != null && !namePart.isEmpty())
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(personRoot.get("name")), "%" + namePart.toLowerCase() + "%"));

        if (minAge != null)
            predicates.add(criteriaBuilder.greaterThan(personRoot.get("age"), minAge));

        if (maxAge != null)
            predicates.add(criteriaBuilder.lessThan(personRoot.get("age"), maxAge));

        if (passport != null && !passport.isEmpty()) {
            Join<PersonEntity, PassportEntity> passportJoin = personRoot.join("passport");
            predicates.add(criteriaBuilder.equal(passportJoin.get("passportNumber"), passport));
        }

        if (city != null && !city.isEmpty()) {
            Join<PersonEntity, AddressEntity> addressJoin = personRoot.join("primaryAddress");
            predicates.add(criteriaBuilder.equal(addressJoin.get("city"), city));
        }

        query.where(criteriaBuilder.and(predicates.toArray(new Predicate[0])));

        return entityManager.createQuery(query).getResultList();
    }

    @Override
    public Long countPersonByCriteria() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = criteriaBuilder.createQuery(Long.class);
        Root<PersonEntity> personRoot = query.from(PersonEntity.class);

        query.select(criteriaBuilder.count(personRoot));

        return entityManager.createQuery(query).getSingleResult();
    }

    @Override
    public List<PersonEntity> findAllWithOffsetAndLimit(Integer offset, Integer limit) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<PersonEntity> query = criteriaBuilder.createQuery(PersonEntity.class);
        Root<PersonEntity> personRoot = query.from(PersonEntity.class);

        query.orderBy(criteriaBuilder.asc(personRoot.get("id")));

        return entityManager.createQuery(query).setMaxResults(limit).setFirstResult(offset).getResultList();
    }

    @Override
    @Transactional
    public int updatePersonAge(Long id, Long age) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

        CriteriaUpdate<PersonEntity> criteriaUpdate = criteriaBuilder.createCriteriaUpdate(PersonEntity.class);
        Root<PersonEntity> personRoot = criteriaUpdate.from(PersonEntity.class);

        criteriaUpdate.set("age", age);

        criteriaUpdate.where(criteriaBuilder.equal(personRoot.get("id"), id));

        return entityManager.createQuery(criteriaUpdate).executeUpdate();
    }

    @Override
    @Transactional
    public int updatePersonAgeHql(Long id, Long age) {
        return entityManager
                .createQuery("UPDATE PersonEntity p SET p.age = :age WHERE p.id = :id")
                .setParameter("age", age)
                .setParameter("id", id)
                .executeUpdate();
    }

}
