package org.example.testrestsoap.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.example.testrestsoap.entity.jpa.AddressEntity;
import org.example.testrestsoap.entity.jpa.PersonEntity;
import org.example.testrestsoap.repository.PersonRepositoryCustom;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class PersonRepositoryCustomImpl implements PersonRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<PersonEntity> findByMultiCriteria(String namePart, Integer minAge, Integer maxAge, String city) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<PersonEntity> query = cb.createQuery(PersonEntity.class);
        Root<PersonEntity> personRoot = query.from(PersonEntity.class);

        List<Predicate> predicates = new ArrayList<>();

        // 1. Фильтр LIKE
        if (namePart != null && !namePart.isEmpty()) {
            predicates.add(cb.like(personRoot.get("name"), "%" + namePart + "%"));
        }

        // 2. Фильтр BETWEEN
        if (minAge != null && maxAge != null) {
            predicates.add(cb.between(personRoot.get("age"), minAge, maxAge));
        }

        // 3. Сравнения Больше/Меньше
        if (minAge != null && maxAge == null) {
            predicates.add(cb.gt(personRoot.get("age"), minAge)); // Strictly Greater Than
        }

        // 4. JOIN ассоциаций
        if (city != null && !city.isEmpty()) {
            Join<PersonEntity, AddressEntity> addressJoin = personRoot.join("primaryAddress");
            predicates.add(cb.equal(addressJoin.get("city"), city));
        }

        // Применяем предикаты в WHERE
        if (!predicates.isEmpty()) {
            query.where(cb.and(predicates.toArray(new Predicate[0])));
        }

        // 5. Сортировка ORDER BY
        query.orderBy(cb.desc(personRoot.get("age")), cb.asc(personRoot.get("name")));

        return entityManager.createQuery(query).getResultList();
    }

    @Override
    public Long countTotalPersonsByCriteria() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = cb.createQuery(Long.class);
        Root<PersonEntity> root = query.from(PersonEntity.class);

        query.select(cb.count(root));

        return entityManager.createQuery(query).getSingleResult();
    }
}
