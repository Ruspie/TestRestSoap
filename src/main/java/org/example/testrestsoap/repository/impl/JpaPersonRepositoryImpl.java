package org.example.testrestsoap.repository.impl;

import jakarta.persistence.*;
import lombok.RequiredArgsConstructor;
import org.example.testrestsoap.entity.jpa.PersonEntity;
import org.example.testrestsoap.repository.JpaPersonRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class JpaPersonRepositoryImpl implements JpaPersonRepository {

    private final SessionFactory sessionFactory;

    @Override
    @Transactional(readOnly = true)
    public PersonEntity findById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            PersonEntity personEntity = session.get(PersonEntity.class, id);
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
        try (Session session = sessionFactory.openSession()) {
            session.persist(personEntity);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    @Transactional
    public void update(PersonEntity personEntity) {
        try (Session session = sessionFactory.openSession()) {
            session.merge(personEntity);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            PersonEntity personEntity = session.get(PersonEntity.class, id);
            if (personEntity != null)
                session.remove(personEntity);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
