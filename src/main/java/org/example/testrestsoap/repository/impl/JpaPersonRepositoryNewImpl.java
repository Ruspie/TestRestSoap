//package org.example.testrestsoap.repository.impl;
//
//import jakarta.persistence.EntityManager;
//import jakarta.persistence.PersistenceContext;
//import jakarta.transaction.Transactional;
//import org.example.testrestsoap.entity.jpa.PersonEntity;
//import org.example.testrestsoap.repository.JpaPersonRepository;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//
//@Repository
//@Transactional // Spring берет на себя управление транзакциями (begin, commit, rollback)
//public class JpaPersonRepositoryNewImpl implements JpaPersonRepository {
//
//    // Spring автоматически внедрит сюда правильный менеджер сущностей
//    @PersistenceContext
//    private EntityManager em;
//
//    //private final EntityManagerFactory emf = Persistence.createEntityManagerFactory("PERSISTENCE");
//
//    // 1. READ (Поиск по ID)
//    @Override
//    @Transactional // Оптимизация для запросов чтения
//    public PersonEntity findById(Long id) {
//        return em.find(PersonEntity.class, id);
//    }
//
//    // 2. CREATE (Сохранение)
//    @Override
//    public void save(PersonEntity person) {
//        em.persist(person); // Перевод из New в Managed
//    }
//
//    // 3. UPDATE (Изменение через Dirty Checking)
//    @Override
//    public void updateName(Long id, String newName) {
//        PersonEntity person = em.find(PersonEntity.class, id); // Объект в состоянии Managed
//        if (person != null) {
//            person.setName(newName); // Изменение применится автоматически при коммите транзакции Спрингом!
//        }
//    }
//
//    // 4. DELETE (Удаление)
//    @Override
//    public void deleteById(Long id) {
//        PersonEntity person = em.find(PersonEntity.class, id);
//        if (person != null) {
//            em.remove(person); // Перевод в Removed
//        }
//    }
//
//    // 5. READ via JPQL
//    @Override
//    @Transactional
//    public List<PersonEntity> findByNameJpql(String name) {
//        return em.createQuery("SELECT p FROM PersonEntity p WHERE p.name = :nameParam", PersonEntity.class)
//                .setParameter("nameParam", name)
//                .getResultList();
//    }
//
//    // 6. BULK DELETE via JPQL
//    @Override
//    public int bulkDeleteCompaniesWithoutWorkers() {
//        return em.createQuery("DELETE FROM CompanyEntity c WHERE c.workers IS EMPTY")
//                .executeUpdate();
//    }
//}