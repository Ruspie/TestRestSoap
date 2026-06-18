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
        EntityTransaction transaction = null;
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            transaction = entityManager.getTransaction();
            transaction.begin();
            PersonEntity personEntity = entityManager.find(PersonEntity.class, id);

            if (personEntity != null) {
                // Принудительно загружаем ленивую коллекцию в память, пока сессия открыта
                personEntity.getWorkingPlaces().size();
            }

            transaction.commit();
            return personEntity;
        } catch (Exception ex) {
            if (transaction != null && transaction.isActive()) transaction.rollback();
            ex.printStackTrace();
            return null;
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
            if (transaction != null && transaction.isActive()) transaction.rollback();
            System.out.println("Ошибка при сохранении: " + e.getMessage());
        }
    }

    @Override
    public void update(PersonEntity personEntity) {
        EntityTransaction transaction = null;
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            transaction = entityManager.getTransaction();
            transaction.begin();

            // merge обновляет состояние отсоединенной (detached) сущности в БД
            entityManager.merge(personEntity);

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) transaction.rollback();
            System.out.println("Ошибка при обновлении: " + e.getMessage());
        }
    }

    @Override
    public void deleteById(Long id) {
        EntityTransaction transaction = null;
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            transaction = entityManager.getTransaction();
            transaction.begin();

            // Чтобы удалить объект, его сначала нужно найти/привязать к текущему EntityManager
            PersonEntity personEntity = entityManager.find(PersonEntity.class, id);
            if (personEntity != null) {
                entityManager.remove(personEntity);
            }

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) transaction.rollback();
            System.out.println("Ошибка при удалении: " + e.getMessage());
        }
    }

    public PersonEntity findByIdJpql(Long id) {
        EntityTransaction transaction = null;
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            transaction = entityManager.getTransaction();
            transaction.begin();

            // Использование JOIN FETCH подгружает ленивые коллекции (workingPlaces)
            // и связи за один оптимальный SQL-запрос (Решает N+1 и LazyInitializationException)
            String jpql = "SELECT p FROM PersonEntity p " +
                          "LEFT JOIN FETCH p.passport " +
                          "LEFT JOIN FETCH p.primaryAddress " +
                          "LEFT JOIN FETCH p.workingPlaces " +
                          "WHERE p.id = :personId";

            PersonEntity personEntity = entityManager.createQuery(jpql, PersonEntity.class)
                .setParameter("personId", id)
                .getSingleResult(); // Бросит NoResultException, если id не найден

            transaction.commit();
            return personEntity;
        } catch (Exception ex) {
            if (transaction != null && transaction.isActive()) transaction.rollback();
            // Возвращаем null, если сущность не найдена или упал эксепшн
            return null;
        }
    }

    public void saveJpql(PersonEntity personEntity) {
        EntityTransaction transaction = null;
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            transaction = entityManager.getTransaction();
            transaction.begin();

            // Для сохранения НОВЫХ сущностей JPQL (INSERT INTO) не поддерживается спецификацией.
            // Стандарт предписывает использовать persist.
            entityManager.persist(personEntity);

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) transaction.rollback();
            System.out.println("Ошибка сохранения: " + e.getMessage());
        }
    }

    public void updateJpql(PersonEntity personEntity) {
        EntityTransaction transaction = null;
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            transaction = entityManager.getTransaction();
            transaction.begin();

            // JPQL-запрос обновления. Обратите внимание: работаем со свойствами класса, а не колонками таблицы!
            String jpql = "UPDATE PersonEntity p SET p.name = :newName WHERE p.id = :personId";

            int updatedRows = entityManager.createQuery(jpql)
                .setParameter("newName", personEntity.getName())
                .setParameter("personId", personEntity.getId())
                .executeUpdate(); // Для UPDATE/DELETE всегда используем executeUpdate()

            System.out.println("[JPQL] Обновлено строк в БД: " + updatedRows);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) transaction.rollback();
            System.out.println("Ошибка обновления через JPQL: " + e.getMessage());
        }
    }

    public void deleteByIdJpql(Long id) {
        EntityTransaction transaction = null;
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            transaction = entityManager.getTransaction();
            transaction.begin();

            // JPQL-запрос удаления. Не требует предварительной загрузки объекта в память!
            String jpql = "DELETE FROM PersonEntity p WHERE p.id = :personId";

            int deletedRows = entityManager.createQuery(jpql)
                .setParameter("personId", id)
                .executeUpdate();

            System.out.println("[JPQL] Удалено строк из БД: " + deletedRows);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) transaction.rollback();
            System.out.println("Ошибка удаления через JPQL: " + e.getMessage());
        }
    }
}
