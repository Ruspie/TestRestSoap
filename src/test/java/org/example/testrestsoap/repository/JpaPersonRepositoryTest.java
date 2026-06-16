package org.example.testrestsoap.repository;

import org.example.testrestsoap.entity.jpa.AddressEntity;
import org.example.testrestsoap.entity.jpa.PassportEntity;
import org.example.testrestsoap.entity.jpa.PersonEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class JpaPersonRepositoryTest {

    @Autowired
    private JpaPersonRepository jpaPersonRepository;

    private Long generatedId;

    @BeforeEach
    public void setUp() {
        // 1. ПОДГОТОВКА ДАННЫХ: Создаем и связываем объекты прямо в коде теста
        PersonEntity newPerson = new PersonEntity();
        newPerson.setName("Alex");

        PassportEntity passport = new PassportEntity();
        passport.setPassportNumber("MP111111");
        newPerson.setPassport(passport); // One-to-One

        AddressEntity address = new AddressEntity();
        address.setCity("Minsk");
        newPerson.setPrimaryAddress(address); // Many-to-One

        // Сохраняем в базу через JPA (сработают каскады CascadeType.ALL / PERSIST)
        jpaPersonRepository.save(newPerson);

        generatedId = newPerson.getId(); // Запоминаем сгенерированный базой ID
    }

    @Test
    void testJpaWorkflow() {
        // 2. READ: Проверяем поиск сохраненного Person по живому ID
        PersonEntity existingPerson = jpaPersonRepository.findById(generatedId);
        Assertions.assertNotNull(existingPerson);
        Assertions.assertEquals("Alex", existingPerson.getName());

        // Проверяем, что связи подтянулись
        Assertions.assertNotNull(existingPerson.getPrimaryAddress());
        Assertions.assertEquals("Minsk", existingPerson.getPrimaryAddress().getCity());
        Assertions.assertEquals("MP111111", existingPerson.getPassport().getPassportNumber());

        // 3. UPDATE: Тестируем изменение данных через Dirty Checking
        jpaPersonRepository.updateName(generatedId, "Robert");
        PersonEntity updatedPerson = jpaPersonRepository.findById(generatedId);
        Assertions.assertEquals("Robert", updatedPerson.getName());

        // 4. READ via JPQL: Тестируем выборку через JPQL запрос из лекции
        List<PersonEntity> jpqlResult = jpaPersonRepository.findByNameJpql("Robert");
        Assertions.assertFalse(jpqlResult.isEmpty());
        Assertions.assertEquals(1, jpqlResult.size());

        // 5. DELETE: Тестируем удаление
        jpaPersonRepository.deleteById(generatedId);
        PersonEntity deletedPerson = jpaPersonRepository.findById(generatedId);
        Assertions.assertNull(deletedPerson); // Объект успешно удален!
    }

}
