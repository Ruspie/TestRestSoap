package org.example.testrestsoap;

import org.example.testrestsoap.entity.jpa.AddressEntity;
import org.example.testrestsoap.entity.jpa.PassportEntity;
import org.example.testrestsoap.entity.jpa.PersonEntity;
import org.example.testrestsoap.repository.JpaPersonRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class TestRestSoapApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(TestRestSoapApplication.class, args);

        JpaPersonRepository jpaPersonRepository = context.getBean(JpaPersonRepository.class);

        PersonEntity personEntityForSave = new PersonEntity();
        PassportEntity passportForSave = new PassportEntity();
        passportForSave.setPassportNumber("122345");
        AddressEntity address = new AddressEntity();
        address.setCity("Minsk");

        personEntityForSave.setPassport(passportForSave);
        personEntityForSave.setPrimaryAddress(address);
        personEntityForSave.setName("Test");

        jpaPersonRepository.save(personEntityForSave);

        PersonEntity personEntity = jpaPersonRepository.findById(1L);

        System.out.println(personEntity.getPassport());
        System.out.println(personEntity.getPrimaryAddress());
        //System.out.println(personEntity.getWorkingPlaces());
    }

}
