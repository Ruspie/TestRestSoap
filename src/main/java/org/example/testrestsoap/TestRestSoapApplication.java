package org.example.testrestsoap;

import org.example.testrestsoap.entity.jpa.PersonEntity;
import org.example.testrestsoap.repository.JpaPersonRepository;
import org.example.testrestsoap.repository.impl.JpaPersonRepositoryImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class TestRestSoapApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(TestRestSoapApplication.class, args);

        //JpaPersonRepositoryImpl jpaPersonRepository = context.getBean(JpaPersonRepositoryImpl.class);
        JpaPersonRepositoryImpl jpaPersonRepository = new JpaPersonRepositoryImpl();

        // 1. ПРОВЕРКА ПОИСКА (findById)
        System.out.println("\n=== Шаг 1: Тест поиска ===");
        PersonEntity personEntity = jpaPersonRepository.findById(1L);

        if (personEntity != null) {
            System.out.println("Паспорт: " + personEntity.getPassport());
            System.out.println("Адрес: " + personEntity.getPrimaryAddress());

            // Выводим только размер, так как объект уже detached.
            // Это сработает, потому что мы вызвали .size() внутри репозитория.
            System.out.println("Количество компаний: " + personEntity.getWorkingPlaces());

            // 2. ПРОВЕРКА ОБНОВЛЕНИЯ (update)
            System.out.println("\n=== Шаг 2: Тест обновления ===");
            personEntity.setName("Alex Updated");
            jpaPersonRepository.update(personEntity);

            // Проверяем, изменилось ли имя в базе данных
            PersonEntity updatedPerson = jpaPersonRepository.findById(1L);
            System.out.println("Новое имя из БД: " + updatedPerson.getName());

            // 3. ПРОВЕРКА УДАЛЕНИЯ (deleteById)
            System.out.println("\n=== Шаг 3: Тест удаления ===");
            jpaPersonRepository.deleteById(1L);

            // Проверяем, что сущности больше нет
            PersonEntity deletedPerson = jpaPersonRepository.findById(1L);
            System.out.println("Результат поиска после удаления (должен быть null): " + deletedPerson);

        } else {
            System.out.println("Пользователь с id 1 не найден. Проверьте выполнение data.sql!");
        }


        /*// 1. ПРОВЕРКА ПОИСКА (findById)
        System.out.println("\n=== Шаг 1: Тест поиска ===");
        PersonEntity personEntityJpql = jpaPersonRepository.findByIdJpql(2L);

        if (personEntityJpql != null) {
            System.out.println("Паспорт: " + personEntityJpql.getPassport());
            System.out.println("Адрес: " + personEntityJpql.getPrimaryAddress());

            // Выводим только размер, так как объект уже detached.
            // Это сработает, потому что мы вызвали .size() внутри репозитория.
            System.out.println("Количество компаний: " + personEntityJpql.getWorkingPlaces());

            // 2. ПРОВЕРКА ОБНОВЛЕНИЯ (update)
            System.out.println("\n=== Шаг 2: Тест обновления ===");
            personEntityJpql.setName("Alex Updated");
            jpaPersonRepository.updateJpql(personEntityJpql);

            // Проверяем, изменилось ли имя в базе данных
            PersonEntity updatedPersonJpql = jpaPersonRepository.findByIdJpql(2L);
            System.out.println("Новое имя из БД: " + updatedPersonJpql.getName());

            // 3. ПРОВЕРКА УДАЛЕНИЯ (deleteById)
            System.out.println("\n=== Шаг 3: Тест удаления ===");
            jpaPersonRepository.deleteByIdJpql(1L);

            // Проверяем, что сущности больше нет
            PersonEntity deletedPersonJpql = jpaPersonRepository.findByIdJpql(1L);
            System.out.println("Результат поиска после удаления (должен быть null): " + deletedPersonJpql);

        } else {
            System.out.println("Пользователь с id 1 не найден. Проверьте выполнение data.sql!");
        }*/
    }

}
