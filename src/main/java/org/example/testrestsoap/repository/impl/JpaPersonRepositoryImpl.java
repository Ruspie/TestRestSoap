package org.example.testrestsoap.repository.impl;

import jakarta.persistence.*;
import lombok.RequiredArgsConstructor;
import org.example.testrestsoap.entity.jpa.PersonEntity;
import org.example.testrestsoap.repository.JpaPersonRepository;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JpaPersonRepositoryImpl implements JpaPersonRepository {

    //private final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("PERSISTENCE");
    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public PersonEntity findById(Long id) {
        try {
            PersonEntity personEntity = entityManager.find(PersonEntity.class, id);
            if (personEntity != null)
                personEntity.getWorkingPlaces().size();
            return personEntity;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void save(PersonEntity personEntity) {
        EntityTransaction transaction = null;
        try {
            transaction = entityManager.getTransaction();

            transaction.begin();
            entityManager.persist(personEntity);

            personEntity.setId(13L);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive())
                transaction.rollback();

            System.out.println(e.getMessage());
        }
    }

    @Override
    public void update(PersonEntity personEntity) {
        EntityTransaction transaction = null;
        try {
            transaction = entityManager.getTransaction();

            transaction.begin();
            entityManager.merge(personEntity);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive())
                transaction.rollback();

            System.out.println(e.getMessage());
        }
    }

    @Override
    public void deleteById(Long id) {
        EntityTransaction transaction = null;
        try {
            transaction = entityManager.getTransaction();

            transaction.begin();

            PersonEntity personEntity = entityManager.find(PersonEntity.class, id);

            if (personEntity != null)
                entityManager.remove(personEntity);

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive())
                transaction.rollback();

            System.out.println(e.getMessage());
        }
    }

    public PersonEntity findByIdJpql(Long id) {
        String query = """
                    SELECT p FROM PersonEntity p
                    LEFT JOIN FETCH p.passport
                    LEFT JOIN FETCH p.primaryAddress
                    LEFT JOIN FETCH p.workingPlaces
                    WHERE p.id = :personId
                """;
        try {
            PersonEntity personEntity = entityManager.createQuery(query, PersonEntity.class)
                    .setParameter("personId", id)
                    .getSingleResult();
//            PersonEntity personEntity = entityManager.find(PersonEntity.class, id);
            if (personEntity != null)
                personEntity.getWorkingPlaces().size();
            return personEntity;
        } catch (Exception e) {
            return null;
        }
    }

    public void saveJpql(PersonEntity personEntity) {
        EntityTransaction transaction = null;
        try {
            transaction = entityManager.getTransaction();

            transaction.begin();

            String query = """
                        INSERT PersonEntity(name, passport, workingPlaces, primaryAddress)
                        VALUES (:name, :passport, :workingPlaces, :primaryAddress)
                    """;

            entityManager.createQuery(query)
                    .setParameter("name", personEntity.getName())
                    .setParameter("passport", personEntity.getPassport())
                    .setParameter("workingPlaces", personEntity.getWorkingPlaces())
                    .setParameter("primaryAddress", personEntity.getPrimaryAddress())
                    .executeUpdate();

            //entityManager.persist(personEntity);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive())
                transaction.rollback();

            System.out.println(e.getMessage());
        }
    }

    public void updateJpql(PersonEntity personEntity) {
        EntityTransaction transaction = null;
        try {
            transaction = entityManager.getTransaction();

            transaction.begin();
            String query = """
                    UPDATE PersonEntity p
                    SET p.name = :name
                    WHERE p.id = :personId
                    """;

            entityManager.createQuery(query)
                    .setParameter("name", personEntity.getName())
                    //.setParameter("passport", personEntity.getPassport())
                    //.setParameter("workingPlaces", personEntity.getWorkingPlaces())
                    //.setParameter("primaryAddress", personEntity.getPrimaryAddress())
                    .setParameter("personId", personEntity.getId())
                    .executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive())
                transaction.rollback();

            System.out.println(e.getMessage());
        }
    }

    public void deleteByIdJpql(Long id) {
        EntityTransaction transaction = null;
        try {
            transaction = entityManager.getTransaction();

            transaction.begin();

            String query = """
                    DELETE FROM PersonEntity p
                    WHERE p.id = :personId
                    """;

            entityManager.createQuery(query)
                    .setParameter("personId", id)
                    .executeUpdate();

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive())
                transaction.rollback();

            System.out.println(e.getMessage());
        }
    }
}
