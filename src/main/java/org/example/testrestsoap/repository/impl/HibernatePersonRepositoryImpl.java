package org.example.testrestsoap.repository.impl;

import org.example.testrestsoap.entity.jpa.PersonEntity;
import org.example.testrestsoap.repository.PersonRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class HibernatePersonRepositoryImpl implements PersonRepository {

    // Вместо EntityManagerFactory используем SessionFactory
    // Конфигурация по умолчанию прочитает файл src/main/resources/hibernate.cfg.xml
    private final SessionFactory sessionFactory = new Configuration()
            .configure() // ищет hibernate.cfg.xml
            .addAnnotatedClass(PersonEntity.class) // явно регистрируем сущность (если не указана в xml)
            .buildSessionFactory();

    // 1. READ (Поиск по ID)
    public PersonEntity findById(Long id) {
        // Сессия в Hibernate реализует AutoCloseable, try-with-resources закроет её автоматически
        try (Session session = sessionFactory.openSession()) {
            return session.get(PersonEntity.class, id); // Вместо em.find() используем session.get()
        }
    }

    // 2. CREATE (Добавление новой сущности)
    public void save(PersonEntity person) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction(); // Начинаем транзакцию Hibernate
            try {
                session.persist(person); // В современном Hibernate тоже используется persist() (вместо старого save())
                tx.commit();
            } catch (Exception e) {
                if (tx.getStatus().canRollback()) tx.rollback(); // Безопасный откат транзакции
                throw e;
            }
        }
    }

    // 3. UPDATE (Изменение данных через Dirty Checking)
    public void updateName(Long id, String newName) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            try {
                PersonEntity person = session.get(PersonEntity.class, id);
                if (person != null) {
                    person.setName(newName); // Dirty Checking работает точно так же внутри транзакции сессии
                }
                tx.commit();
            } catch (Exception e) {
                if (tx.getStatus().canRollback()) tx.rollback();
                throw e;
            }
        }
    }

    // 4. DELETE (Удаление сущности)
    public void deleteById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            try {
                PersonEntity person = session.get(PersonEntity.class, id);
                if (person != null) {
                    session.remove(person); // Вместо em.remove() используем session.remove() (или session.delete())
                }
                tx.commit();
            } catch (Exception e) {
                if (tx.getStatus().canRollback()) tx.rollback();
                throw e;
            }
        }
    }

    // 5. READ via HQL (Вместо JPQL)
    public List<PersonEntity> findByNameJpql(String name) {
        try (Session session = sessionFactory.openSession()) {
            // Синтаксис HQL полностью совпадает с JPQL, но возвращает Query из пакета org.hibernate.query
            return session.createQuery("SELECT p FROM PersonEntity p WHERE p.name = :nameParam", PersonEntity.class)
                    .setParameter("nameParam", name)
                    .getResultList();
        }
    }

    // 6. BULK DELETE via HQL
    public int bulkDeleteCompaniesWithoutWorkers() {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            try {
                int deletedCount = session.createQuery("DELETE FROM CompanyEntity c WHERE c.workers IS EMPTY", null)
                        .executeUpdate();
                tx.commit();
                return deletedCount;
            } catch (Exception e) {
                if (tx.getStatus().canRollback()) tx.rollback();
                throw e;
            }
        }
    }
}
