package org.example.testrestsoap.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import org.example.testrestsoap.entity.jpa.PersonEntity;
import org.example.testrestsoap.repository.JpaPersonRepository;
import org.springframework.stereotype.Repository;

@Repository
public class JpaPersonRepositoryImpl implements JpaPersonRepository {

    private final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("PERSISTENCE");

    @Override
    public PersonEntity findById(Long id) {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            return entityManager.find(PersonEntity.class, id);
        }
    }

    @Override
    public void save(PersonEntity personEntity) {
        EntityTransaction transaction = null;
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            transaction = entityManager.getTransaction();

            transaction.begin();
            entityManager.persist(personEntity);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive())
                transaction.rollback();

            System.out.println(e.getMessage());
        }
    }

    @Override
    public void updateName(Long id, String name) {

    }

    @Override
    public void deleteById(Long id) {

    }
}
