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

        List<PersonEntity> persons = jpaPersonRepository.findAllHql();

        System.out.println(persons);

        List<Object[]> personFields = jpaPersonRepository.findNamesAndAgesHql();
        for (Object[] personField : personFields) {
            System.out.println("Name: " + personField[0] + " age: " + personField[1]);
        }

        List<PersonEntity> ivans = jpaPersonRepository.findByNameAndAgeGreaterThanHql("Ivan", 15L);

        List<PersonEntity> petrs = jpaPersonRepository.findByNameAndAgeGreaterThanHql("Petr", 10L);

        System.out.println(ivans);
        System.out.println(petrs);


        List<PersonEntity> allOrderedHql = jpaPersonRepository.findAllOrderedHql();

        System.out.println(allOrderedHql);

        Object[] aggregatedInfoHql = jpaPersonRepository.getAggregatedInfoHql();
        for (int i = 0; i < ((Object[]) aggregatedInfoHql[0]).length; i++) {
            System.out.print(((Object[]) aggregatedInfoHql[0])[i].toString() + " ");
        }

        List<Object[]> cityStats = jpaPersonRepository.getCityStatHql();
        for (Object[] cityStat : cityStats) {
            System.out.println("City: " + cityStat[0] + " count: " + cityStat[1]);
        }

        List<PersonEntity> allOlderThanAverageAge = jpaPersonRepository.findAllOlderThanAverageAgeHql();
        System.out.println(allOlderThanAverageAge);

        List<PersonEntity> passportNumber = jpaPersonRepository.findByPassportNumberHql("MP111111");

        System.out.println(passportNumber);

        Optional<PersonEntity> mp111111 = jpaPersonRepository.findByPassportNumberOptionalHql("MP111111");
        if (mp111111.isPresent())
            System.out.println("Есть MP111111" + mp111111.get());
        else
            System.out.println("Нет MP111111");

        Optional<PersonEntity> mp123 = jpaPersonRepository.findByPassportNumberOptionalHql("MP123");
        if (mp123.isPresent())
            System.out.println("Есть mp123" + mp123.get());
        else
            System.out.println("Нет mp123");

        List<PersonEntity> byMultiConditionsCriteriaNulls = jpaPersonRepository.findByMultiConditionsCriteria(null, null, null, null, null, null);
        System.out.println(byMultiConditionsCriteriaNulls);

        List<PersonEntity> byMultiConditionsCriteriaName = jpaPersonRepository.findByMultiConditionsCriteria("Petr", null, null, null, null, null);
        System.out.println(byMultiConditionsCriteriaName);

        List<PersonEntity> byMultiConditionsCriteriaNamePart = jpaPersonRepository.findByMultiConditionsCriteria(null, "i", null, null, null, null);
        System.out.println(byMultiConditionsCriteriaNamePart);

        System.out.println(
                jpaPersonRepository.findByMultiConditionsCriteria(
                        "Alex", "Al", 20L,
                        25L, "MP444444", "Minsk"
                )
        );

        System.out.println(jpaPersonRepository.countPersonByCriteria());

        System.out.println(jpaPersonRepository.findAllWithOffsetAndLimit(3, 2));

        //persons.get(0).setAge(100L);
        //jpaPersonRepository.save(persons.get(0));

        System.out.println("affectedRows" + jpaPersonRepository.updatePersonAgeHql(4L, 100L));

       // jpaPersonRepository.delete(persons.get(0));

        System.out.println(jpaPersonRepository.findByMultiConditionsCriteria(null, null, 99L, 101L, null, null));

        //jpaPersonRepository.delete(persons.get(0));

        //System.out.println(jpaPersonRepository.findByMultiConditionsCriteria(null, null, 99L, 101L, null, null));

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
