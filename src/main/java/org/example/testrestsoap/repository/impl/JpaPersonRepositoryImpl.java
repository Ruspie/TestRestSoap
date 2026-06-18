package org.example.testrestsoap.repository.impl;

import jakarta.persistence.*;
import lombok.RequiredArgsConstructor;
import org.example.testrestsoap.entity.jpa.PersonEntity;
import org.example.testrestsoap.repository.JpaPersonRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class JpaPersonRepositoryImpl implements JpaPersonRepository {

    //private final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("PERSISTENCE");
    @PersistenceContext
    private EntityManager entityManager;


    @Override
    @Transactional
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
    @Transactional
    public void save(PersonEntity personEntity) {
        try {
            entityManager.persist(personEntity);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Transactional
    @Override
    public void update(PersonEntity personEntity) {
        try {
            entityManager.merge(personEntity);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        try {
            PersonEntity personEntity = entityManager.find(PersonEntity.class, id);

            if (personEntity != null)
                entityManager.remove(personEntity);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
