package org.example.testrestsoap.controller;

import lombok.RequiredArgsConstructor;
import org.example.test.generated.TestIncrementRequestDto;
import org.example.testrestsoap.service.TestService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
@ResponseStatus(HttpStatus.OK)
public class TestRestController {

    private final TestService testService;

    @PostMapping("/increment")
    public ResponseEntity<?> incrementCounter(@RequestBody TestIncrementRequestDto testRequestDto) {
        return new ResponseEntity<>(testService.incrementCounter(testRequestDto), HttpStatus.OK);
    }

    @GetMapping("/counter/{counterId}")
    public ResponseEntity getCounter(@PathVariable Long counterId) {
        return new ResponseEntity<>(testService.getCounterById(counterId), HttpStatus.OK);
    }

}
