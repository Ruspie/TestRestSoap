package org.example.testrestsoap;

import org.example.testrestsoap.entity.jpa.PersonEntity;
import org.example.testrestsoap.repository.JpaPersonRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.List;
import java.util.Optional;

@SpringBootApplication
public class TestRestSoapApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(TestRestSoapApplication.class, args);

        JpaPersonRepository jpaPersonRepository = context.getBean(JpaPersonRepository.class);
        //JpaPersonRepositoryImpl jpaPersonRepository = new JpaPersonRepositoryImpl();

        System.out.println("\n====== ПРОВЕРКА МЕХАНИЗМА ПО ИМЕНИ МЕТОДА ======");
        System.out.println("Поиск по имени 'Ivan': " + jpaPersonRepository.findByName("Ivan"));
        System.out.println("Поиск по возрасту 20-25: " + jpaPersonRepository.findByAgeBetween(20L, 25L));
        System.out.println("Поиск жителей Минска: " + jpaPersonRepository.findByPrimaryAddressCity("Minsk").size());
        System.out.println("Существует ли паспорт MP111111: " + jpaPersonRepository.existsByPassportPassportNumber("MP111111"));


        System.out.println("\n====== ПРОВЕРКА ТЕХНОЛОГИИ QUERYDSL ======");
        // Тестируем наш сложный динамический фильтр через QueryDSL на данных из data.sql
        List<PersonEntity> queryDslResult = jpaPersonRepository.findByMultiConditionsQueryDsl(
                "Alex", "Al", 20L, 25L, "MP444444", "Minsk"
        );
        System.out.println("QueryDSL нашёл Алекса: " + queryDslResult);

        // Проверяем пустой фильтр QueryDSL (должен вернуть всех 4 человек)
        List<PersonEntity> queryDslAll = jpaPersonRepository.findByMultiConditionsQueryDsl(null, null, null, null, null, null);
        System.out.println("QueryDSL без фильтров (все люди): " + queryDslAll.size());


        System.out.println("\n====== ПРОВЕРКА ПРОДВИНУТОГО QUERYDSL ======");

        // 1. Тестируем пагинацию: пропускаем 1 человека, берем следующих 2
        List<PersonEntity> dslPage = jpaPersonRepository.findPersonsWithPaginationQueryDsl(1, 2);
        System.out.println("QueryDSL Пагинация (взяли 2 строки со смещением 1):");
        dslPage.forEach(p -> System.out.println(" - " + p.getName() + " (ID: " + p.getId() + ")"));

        // 2. Тестируем JOIN FETCH
        jpaPersonRepository.findByPassportNumberWithFetchJoinQueryDsl("MP111111").ifPresent(person -> {
            System.out.println("QueryDSL JOIN FETCH -> Нашли человека: " + person.getName());
            System.out.println("Его паспорт выкачан сразу: " + person.getPassport().getPassportNumber());
        });

        // 3. Тестируем GROUP BY + HAVING + Агрегаты
        // Ищем города, где средний возраст жителей больше 18 лет
        List<com.querydsl.core.Tuple> stats = jpaPersonRepository.getCityStatsWithHavingQueryDsl(18.0);
        System.out.println("QueryDSL Агрегация и Группировка:");
        for (com.querydsl.core.Tuple row : stats) {
            // Маппим данные из Tuple по исходным Q-полям
            String cityName = row.get(org.example.testrestsoap.entity.jpa.QPersonEntity.personEntity.primaryAddress.city);
            Long totalCount = row.get(org.example.testrestsoap.entity.jpa.QPersonEntity.personEntity.count());
            Double averageAge = row.get(org.example.testrestsoap.entity.jpa.QPersonEntity.personEntity.age.avg());

            System.out.println(" - Город: " + cityName + " | Всего жителей: " + totalCount + " | Средний возраст: " + averageAge);
        }

        System.out.println("\n====== ПРОВЕРКА DISTINCT В QUERYDSL ======");

        List<String> uniqueCities = jpaPersonRepository.findUniqueCitiesQueryDsl();
        System.out.println("Список уникальных городов проживания из базы (DISTINCT):");
        uniqueCities.forEach(city -> System.out.println(" - " + city));

//        List<PersonEntity> persons = jpaPersonRepository.findAllHql();
//
//        System.out.println(persons);
//
//        List<Object[]> personFields = jpaPersonRepository.findNamesAndAgesHql();
//        for (Object[] personField : personFields) {
//            System.out.println("Name: " + personField[0] + " age: " + personField[1]);
//        }
//
//        List<PersonEntity> ivans = jpaPersonRepository.findByNameAndAgeGreaterThanHql("Ivan", 15L);
//
//        List<PersonEntity> petrs = jpaPersonRepository.findByNameAndAgeGreaterThanHql("Petr", 10L);
//
//        System.out.println(ivans);
//        System.out.println(petrs);
//
//
//        List<PersonEntity> allOrderedHql = jpaPersonRepository.findAllOrderedHql();
//
//        System.out.println(allOrderedHql);
//
//        Object[] aggregatedInfoHql = jpaPersonRepository.getAggregatedInfoHql();
//        for (int i = 0; i < ((Object[]) aggregatedInfoHql[0]).length; i++) {
//            System.out.print(((Object[]) aggregatedInfoHql[0])[i].toString() + " ");
//        }
//
//        List<Object[]> cityStats = jpaPersonRepository.getCityStatHql();
//        for (Object[] cityStat : cityStats) {
//            System.out.println("City: " + cityStat[0] + " count: " + cityStat[1]);
//        }
//
//        List<PersonEntity> allOlderThanAverageAge = jpaPersonRepository.findAllOlderThanAverageAgeHql();
//        System.out.println(allOlderThanAverageAge);
//
//        List<PersonEntity> passportNumber = jpaPersonRepository.findByPassportNumberHql("MP111111");
//
//        System.out.println(passportNumber);
//
//        Optional<PersonEntity> mp111111 = jpaPersonRepository.findByPassportNumberOptionalHql("MP111111");
//        if (mp111111.isPresent())
//            System.out.println("Есть MP111111" + mp111111.get());
//        else
//            System.out.println("Нет MP111111");
//
//        Optional<PersonEntity> mp123 = jpaPersonRepository.findByPassportNumberOptionalHql("MP123");
//        if (mp123.isPresent())
//            System.out.println("Есть mp123" + mp123.get());
//        else
//            System.out.println("Нет mp123");
//
//        List<PersonEntity> byMultiConditionsCriteriaNulls = jpaPersonRepository.findByMultiConditionsCriteria(null, null, null, null, null, null);
//        System.out.println(byMultiConditionsCriteriaNulls);
//
//        List<PersonEntity> byMultiConditionsCriteriaName = jpaPersonRepository.findByMultiConditionsCriteria("Petr", null, null, null, null, null);
//        System.out.println(byMultiConditionsCriteriaName);
//
//        List<PersonEntity> byMultiConditionsCriteriaNamePart = jpaPersonRepository.findByMultiConditionsCriteria(null, "i", null, null, null, null);
//        System.out.println(byMultiConditionsCriteriaNamePart);
//
//        System.out.println(
//                jpaPersonRepository.findByMultiConditionsCriteria(
//                        "Alex", "Al", 20L,
//                        25L, "MP444444", "Minsk"
//                )
//        );
//
//        System.out.println(jpaPersonRepository.countPersonByCriteria());
//
//        System.out.println(jpaPersonRepository.findAllWithOffsetAndLimit(3, 2));
//
//        //persons.get(0).setAge(100L);
//        //jpaPersonRepository.save(persons.get(0));
//
//        System.out.println("affectedRows" + jpaPersonRepository.updatePersonAgeHql(4L, 100L));
//
//       // jpaPersonRepository.delete(persons.get(0));
//
//        System.out.println(jpaPersonRepository.findByMultiConditionsCriteria(null, null, 99L, 101L, null, null));
//
//        //jpaPersonRepository.delete(persons.get(0));
//
//        //System.out.println(jpaPersonRepository.findByMultiConditionsCriteria(null, null, 99L, 101L, null, null));

//        PersonEntity personEntityForSave = new PersonEntity();
//        PassportEntity passportForSave = new PassportEntity();
//        passportForSave.setPassportNumber("122345");
//        AddressEntity address = new AddressEntity();
//        address.setCity("Minsk");
//
//        personEntityForSave.setPassport(passportForSave);
//        personEntityForSave.setPrimaryAddress(address);
//        personEntityForSave.setName("Test");
//
//        jpaPersonRepository.save(personEntityForSave);
//
//        PersonEntity personEntitySaved = jpaPersonRepository.findById(3L);
//
//        System.out.println(personEntitySaved);
//
//        PersonEntity personEntity = jpaPersonRepository.findById(1L);
//
//        personEntity.getWorkingPlaces().size();
//
//        System.out.println(personEntity);
//        System.out.println(personEntity.getPassport());
//        System.out.println(personEntity.getPrimaryAddress());
//        System.out.println(personEntity.getWorkingPlaces());
//
//        personEntity.setName("Alexander");
//
//        jpaPersonRepository.update(personEntity);
//
//        personEntity.setName("TEST");
//
//        PersonEntity personEntityAfterUpdate = jpaPersonRepository.findById(1L);
//
//        System.out.println(personEntity);
//        System.out.println(personEntityAfterUpdate);
//
//        jpaPersonRepository.deleteById(personEntityAfterUpdate.getId());
//
//        PersonEntity personEntityAfterDelete = jpaPersonRepository.findById(1L);
//        System.out.println(personEntityAfterDelete);
    }

}
