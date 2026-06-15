package org.example.testrestsoap.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.example.testrestsoap.entity.jpa.PersonEntity;
import org.example.testrestsoap.repository.JpaPersonRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Transactional
@Repository
public class JpaPersonRepositoryNewImpl implements JpaPersonRepository {

    // Spring сам создаст EntityManager, подключенный к той же сессии с data.sql!
    @PersistenceContext
    private EntityManager em;

    public PersonEntity findById(Long id) {
        return em.find(PersonEntity.class, id); // Теперь тут Alex из data.sql найдется!
    }

    public void save(PersonEntity person) {
        em.persist(person);
    }

    public void updateName(Long id, String newName) {
        PersonEntity person = em.find(PersonEntity.class, id);
        if (person != null) {
            person.setName(newName);
        }
    }

    public void deleteById(Long id) {
        PersonEntity person = em.find(PersonEntity.class, id);
        if (person != null) {
            em.remove(person);
        }
    }

    public List<PersonEntity> findByNameJpql(String name) {
        return em.createQuery("SELECT p FROM PersonEntity p WHERE p.name = :nameParam", PersonEntity.class)
                .setParameter("nameParam", name)
                .getResultList();
    }

    @Override
    public int bulkDeleteCompaniesWithoutWorkers() {
        return em.createQuery("DELETE FROM CompanyEntity c WHERE c.workers IS EMPTY")
                .executeUpdate();
    }

}
