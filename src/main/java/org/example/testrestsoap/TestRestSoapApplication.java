package org.example.testrestsoap;

import org.example.testrestsoap.entity.jpa.AddressEntity;
import org.example.testrestsoap.entity.jpa.PassportEntity;
import org.example.testrestsoap.entity.jpa.PersonEntity;
import org.example.testrestsoap.repository.PersonRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.List;

@SpringBootApplication
public class TestRestSoapApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(TestRestSoapApplication.class, args);
        PersonRepository personRepository = context.getBean(PersonRepository.class);

        AddressEntity moscow = new AddressEntity();
        moscow.setCity("Moscow");
        AddressEntity minsk = new AddressEntity();
        minsk.setCity("Minsk");

        PersonEntity person1 = new PersonEntity();
        person1.setName("Ivan");
        person1.setAge(20);
        person1.setPrimaryAddress(minsk.clone());
        PersonEntity person2 = new PersonEntity();
        person2.setName("Alex");
        person2.setAge(25);
        person2.setPrimaryAddress(minsk.clone());
        PersonEntity person3 = new PersonEntity();
        person3.setName("Petr");
        person3.setAge(17);
        person3.setPrimaryAddress(moscow);

        personRepository.save(person1);
        personRepository.save(person2);
        personRepository.save(person3);

        List<Object[]> projections = personRepository.findNamesAndAgesHql();
        System.out.println("HQL Проекция (Имя + Возраст первый в списке): " + projections.get(0)[0] + ", " + projections.get(0)[1]);

        Object[] stats = personRepository.getAggregateStatsHql();
        System.out.println("HQL Агрегаты -> Всего: " + ((Object[])stats[0])[0] + ", Ср. возраст: " + ((Object[])stats[0])[1] + ", Сумма возрастов: " + ((Object[])stats[0])[2]);

        List<Object[]> havingStats = personRepository.getCityStatsWithHavingHql();
        System.out.println("HQL HAVING -> Городов со средним возрастом > 18: " + havingStats.size());

        List<PersonEntity> olderThanAvg = personRepository.findOlderThanAverageHql();
        System.out.println("HQL Подзапрос -> Людей старше среднего возраста: " + olderThanAvg.size());

        List<PersonEntity> criteriaResult = personRepository.findByMultiCriteria("Al", 18, 30, "Minsk");
        System.out.println("Criteria (LIKE 'Al' + BETWEEN 18-30): нашли " + criteriaResult.size() + " чел.");
        if (!criteriaResult.isEmpty()) {
            System.out.println("Найден: " + criteriaResult.get(0).getName());
        }

        Long count = personRepository.countTotalPersonsByCriteria();
        System.out.println("Criteria Агрегат -> Общее количество: " + count);
    }
}
