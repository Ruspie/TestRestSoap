package org.example.testrestsoap.service;

import lombok.RequiredArgsConstructor;
import org.example.testrestsoap.entity.jpa.PersonEntity;
import org.example.testrestsoap.repository.JpaPersonRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PersonService {

    private final JpaPersonRepository personRepository;

    public List<PersonEntity> getPersonsWithPagination(int limit, int offset) {
        return personRepository.findPersonsWithPaginationQueryDsl(offset, limit);
    }

    public void updateAge(Long personId, Long age) {
        personRepository.updatePersonAge(personId, age);
    }

    public List<PersonEntity> getAllPersons() {
        return personRepository.findAll();
    }
}
