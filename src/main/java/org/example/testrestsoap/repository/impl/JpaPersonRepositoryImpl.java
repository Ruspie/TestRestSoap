package org.example.testrestsoap.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import org.example.testrestsoap.entity.jpa.PersonEntity;
import org.example.testrestsoap.repository.PersonRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class JpaPersonRepositoryImpl implements PersonRepository {

    private final EntityManagerFactory emf = Persistence.createEntityManagerFactory("PERSISTENCE");

    // 1. READ (Поиск по ID)
    public PersonEntity findById(Long id) {
        try (EntityManager em = emf.createEntityManager()) {
            return em.find(PersonEntity.class, id);
        }
    }

    // 2. CREATE (Добавление новой сущности)
    public void save(PersonEntity person) {
        try (EntityManager em = emf.createEntityManager()) {
            EntityTransaction tx = em.getTransaction();
            try {
                tx.begin();
                em.persist(person);
                tx.commit();
            } catch (Exception e) {
                if (tx.isActive()) tx.rollback(); // Откат происходит, пока EM ещё открыт
                throw e;
            }
        }
    }

    // 3. UPDATE (Изменение данных через Dirty Checking)
    public void updateName(Long id, String newName) {
        try (EntityManager em = emf.createEntityManager()) {
            EntityTransaction tx = em.getTransaction();
            try {
                tx.begin();
                PersonEntity person = em.find(PersonEntity.class, id);
                if (person != null) {
                    person.setName(newName);
                }
                tx.commit();
            } catch (Exception e) {
                if (tx.isActive()) tx.rollback();
                throw e;
            }
        }
    }

    // 4. DELETE (Удаление сущности)
    public void deleteById(Long id) {
        try (EntityManager em = emf.createEntityManager()) {
            EntityTransaction tx = em.getTransaction();
            try {
                tx.begin();
                PersonEntity person = em.find(PersonEntity.class, id);
                if (person != null) {
                    em.remove(person);
                }
                tx.commit();
            } catch (Exception e) {
                if (tx.isActive()) tx.rollback();
                throw e;
            }
        }
    }

    // 5. READ via JPQL
    public List<PersonEntity> findByNameJpql(String name) {
        try (EntityManager em = emf.createEntityManager()) {
            return em.createQuery("SELECT p FROM PersonEntity p WHERE p.name = :nameParam", PersonEntity.class)
                    .setParameter("nameParam", name)
                    .getResultList();
        }
    }

    // 6. BULK DELETE via JPQL
    public int bulkDeleteCompaniesWithoutWorkers() {
        try (EntityManager em = emf.createEntityManager()) {
            EntityTransaction tx = em.getTransaction();
            try {
                tx.begin();
                int deletedCount = em.createQuery("DELETE FROM CompanyEntity c WHERE c.workers IS EMPTY")
                        .executeUpdate();
                tx.commit();
                return deletedCount;
            } catch (Exception e) {
                if (tx.isActive()) tx.rollback();
                throw e;
            }
        }
    }
}