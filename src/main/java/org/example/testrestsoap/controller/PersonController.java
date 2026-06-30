package org.example.testrestsoap.controller;

import lombok.RequiredArgsConstructor;
import org.example.testrestsoap.entity.jpa.PersonEntity;
import org.example.testrestsoap.service.PersonService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/persons")
@RequiredArgsConstructor
public class PersonController {

    private final PersonService personService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<PersonEntity>> getPersonsWithPagination(@RequestParam(defaultValue = "10") int limit, @RequestParam(defaultValue = "0") int offset) {
        return ResponseEntity.ok().body(personService.getPersonsWithPagination(limit, offset));
    }

    @PutMapping("/{id}/age")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> updateAge(@PathVariable(name = "id") Long personId, @RequestParam Long age) {
        personService.updateAge(personId, age);
        return ResponseEntity.ok("Возраст обновлен");
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<PersonEntity>> getAllPersons() {
        return ResponseEntity.ok().body(personService.getAllPersons());
    }

}
