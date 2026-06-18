package org.example.testrestsoap.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import org.example.testrestsoap.entity.jpa.PersonEntity;
import org.example.testrestsoap.repository.JpaPersonRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class JpaPersonRepositoryImpl implements JpaPersonRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public PersonEntity findById(Long id) {
        PersonEntity personEntity = entityManager.find(PersonEntity.class, id);

        if (personEntity != null) {
            // Принудительно загружаем ленивую коллекцию в память, пока сессия открыта
            personEntity.getWorkingPlaces().size();
        }

        return personEntity;
    }

    @Override
    @Transactional
    public void save(PersonEntity personEntity) {
        entityManager.persist(personEntity);
    }

    @Override
    @Transactional
    public void update(PersonEntity personEntity) {
        // merge обновляет состояние отсоединенной (detached) сущности в БД
        entityManager.merge(personEntity);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        PersonEntity personEntity = entityManager.find(PersonEntity.class, id);
        if (personEntity != null) {
            entityManager.remove(personEntity);
        }
    }

    @Transactional(readOnly = true)
    public PersonEntity findByIdJpql(Long id) {
        String jpql = "SELECT p FROM PersonEntity p " +
                      "LEFT JOIN FETCH p.passport " +
                      "LEFT JOIN FETCH p.primaryAddress " +
                      "LEFT JOIN FETCH p.workingPlaces " +
                      "WHERE p.id = :personId";

        try {
            PersonEntity personEntity = entityManager.createQuery(jpql, PersonEntity.class)
                .setParameter("personId", id)
                .getSingleResult();
            return personEntity;
        } catch (NoResultException e) {
            return null;
        }

    }

    @Transactional
    public void saveJpql(PersonEntity personEntity) {
        // Для сохранения НОВЫХ сущностей JPQL (INSERT INTO) не поддерживается спецификацией.
        // Стандарт предписывает использовать persist.
        entityManager.persist(personEntity);
    }

    @Transactional
    public void updateJpql(PersonEntity personEntity) {
        // JPQL-запрос обновления. Обратите внимание: работаем со свойствами класса, а не колонками таблицы!
        String jpql = "UPDATE PersonEntity p SET p.name = :newName WHERE p.id = :personId";

        int updatedRows = entityManager.createQuery(jpql)
            .setParameter("newName", personEntity.getName())
            .setParameter("personId", personEntity.getId())
            .executeUpdate(); // Для UPDATE/DELETE всегда используем executeUpdate()
    }

    @Transactional
    public void deleteByIdJpql(Long id) {
        String jpql = "DELETE FROM PersonEntity p WHERE p.id = :personId";

        int deletedRows = entityManager.createQuery(jpql)
            .setParameter("personId", id)
            .executeUpdate();
    }

}
