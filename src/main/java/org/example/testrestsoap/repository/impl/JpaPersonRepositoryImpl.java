package org.example.testrestsoap.repository.impl;

import lombok.RequiredArgsConstructor;
import org.example.testrestsoap.entity.jpa.PersonEntity;
import org.example.testrestsoap.repository.JpaPersonRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

//@Repository
@RequiredArgsConstructor
public class JpaPersonRepositoryImpl implements JpaPersonRepository {

    // Создаем фабрику сессий на основе hibernate.cfg.xml
    private final SessionFactory sessionFactory = new Configuration()
        .configure("hibernate.cfg.xml")
        .buildSessionFactory();

    //private final SessionFactory sessionFactory;

    @Override
    public PersonEntity findById(Long id) {
        Transaction transaction = null;
        // Открываем сессию Hibernate
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();

            // Ищем сущность по ID
            PersonEntity personEntity = session.get(PersonEntity.class, id);

            if (personEntity != null) {
                // Принудительно подгружаем ленивую коллекцию, пока сессия открыта
                personEntity.getWorkingPlaces().size();
            }

            transaction.commit();
            return personEntity;
        } catch (Exception ex) {
            if (transaction != null) transaction.rollback();
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public void save(PersonEntity personEntity) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();

            // Операция добавления (сохранения) в Hibernate
            session.persist(personEntity);

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            System.out.println("Ошибка при добавлении: " + e.getMessage());
        }
    }

    @Override
    public void update(PersonEntity personEntity) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();

            // Операция изменения (обновления). Склеивает отсоединенный объект с базой
            session.merge(personEntity);

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            System.out.println("Ошибка при изменении: " + e.getMessage());
        }
    }

    @Override
    public void deleteById(Long id) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();

            // Сначала находим объект в текущей сессии
            PersonEntity personEntity = session.get(PersonEntity.class, id);
            if (personEntity != null) {
                // Операция удаления
                session.remove(personEntity);
            }

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            System.out.println("Ошибка при удалении: " + e.getMessage());
        }
    }
}
