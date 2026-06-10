package org.example.testrestsoap.controller;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.testrestsoap.dto.ErrorResponseDto;
import org.example.testrestsoap.dto.FileResponseDto;
import org.example.testrestsoap.dto.TestIncrementRequestDto;
import org.example.testrestsoap.dto.TestResponseDto;
import org.example.testrestsoap.service.TestService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Counter", description = "Запросы для работы со счетчиком")
@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
@ResponseStatus(HttpStatus.OK)
public class TestRestController {

    private final TestService testService;

    @Hidden
    @PostMapping("/increment")
    public ResponseEntity<?> incrementCounter(@RequestBody TestIncrementRequestDto testRequestDto) {
        return new ResponseEntity<>(testService.incrementCounter(testRequestDto), HttpStatus.OK);
    }

    @Operation(summary = "Получение счетчика", description = "Получение информации по счетчику по id")
    @ApiResponse(responseCode = "200", description = "Успешное выполнение запроса", content = @Content(
            mediaType = "application/json", schema = @Schema(implementation = TestResponseDto.class)
    ))
    @ApiResponse(responseCode = "400", description = "Выполнение запроса с ошибкой", content = @Content(
            mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDto.class)
    ))
    @GetMapping("/counter/{counterId}")
    public ResponseEntity getCounter(@PathVariable Long counterId) {
        return new ResponseEntity<>(testService.getCounterById(counterId), HttpStatus.OK);
    }

}
