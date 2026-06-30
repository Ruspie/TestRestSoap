package org.example.testrestsoap.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.testrestsoap.entity.jpa.PersonEntity;
import org.example.testrestsoap.repository.JpaPersonRepository;
import org.example.testrestsoap.repository.JpaPersonRepositoryQueryDsl;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class PersonService {

    private final JpaPersonRepository personRepository;

    public PersonService(@Qualifier("jpaPersonRepository") JpaPersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public List<PersonEntity> getPersonsWithPagination(int offset, int limit) {
        log.info("Fetching chunk of persons with offset: {}, limit: {}", offset, limit);
        return personRepository.findPersonsWithPaginationQueryDsl(offset, limit);
    }

    public Optional<PersonEntity> getPersonByPassport(String passportNumber) {
        log.info("Searching person by passport number");
        return personRepository.findPersonByPassportNumberQueryDsl(passportNumber);
    }

    public List<PersonEntity> searchPersons(String name, String namePart, Long minAge, Long maxAge, String city) {
        log.info("Searching persons by dynamic criteria");
        return personRepository.findByMultiConditionsQueryDsl(name, namePart, minAge, maxAge, null, city);
    }

    @Transactional
    public void updateAge(Long id, Long newAge) {
        log.info("Updating age for person id: {}", id);
        int updatedRows = personRepository.updatePersonAgeHql(id, newAge);
        if (updatedRows == 0) {
            throw new RuntimeException("Person not found with id: " + id);
        }
    }

    @Transactional
    public PersonEntity savePerson(PersonEntity person) {
        log.info("Saving new person to database");
        return personRepository.save(person);
    }
}
