package org.example.testrestsoap.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.testrestsoap.entity.jpa.PersonEntity;
import org.example.testrestsoap.service.PersonService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Person", description = "Управление персональными данными людей")
@RestController
@RequestMapping("/api/persons")
@RequiredArgsConstructor
@SecurityRequirement(name = "SessionAuth") // Привязка к замку в Swagger UI
public class PersonRestController {

    private final PersonService personService;

    @Operation(summary = "Постраничный вывод людей", description = "Доступно ролям USER и ADMIN")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping
    public ResponseEntity<List<PersonEntity>> getWithPagination(@RequestParam(defaultValue = "0") int offset,
                                                                @RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(personService.getPersonsWithPagination(offset, limit));
    }

    @Operation(summary = "Динамический поиск", description = "Поиск без раскрытия паспорта. Доступно ролям USER и ADMIN")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/search")
    public ResponseEntity<List<PersonEntity>> search(@RequestParam(required = false) String name,
                                                     @RequestParam(required = false) String namePart,
                                                     @RequestParam(required = false) Long minAge,
                                                     @RequestParam(required = false) Long maxAge,
                                                     @RequestParam(required = false) String city) {
        return ResponseEntity.ok(personService.searchPersons(name, namePart, minAge, maxAge, city));
    }

    @Operation(summary = "Поиск по паспорту", description = "Критическая операция! Доступно ТОЛЬКО роли ADMIN")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/by-passport")
    public ResponseEntity<PersonEntity> getByPassport(@RequestParam String passportNumber) {
        return personService.getPersonByPassport(passportNumber)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @Operation(summary = "Обновление возраста человека", description = "Доступно ТОЛЬКО роли ADMIN")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/age")
    public ResponseEntity<String> updateAge(@PathVariable Long id, @RequestParam Long age) {
        personService.updateAge(id, age);
        return ResponseEntity.ok("Возраст успешно обновлен");
    }

    @Operation(summary = "Создание новой карточки человека", description = "Доступно ТОЛЬКО роли ADMIN")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<PersonEntity> create(@RequestBody PersonEntity person) {
        return ResponseEntity.status(HttpStatus.CREATED).body(personService.savePerson(person));
    }
}
