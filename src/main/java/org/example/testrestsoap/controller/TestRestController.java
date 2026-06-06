package org.example.testrestsoap.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.testrestsoap.dto.ErrorResponseDto;
import org.example.testrestsoap.dto.TestIncrementRequestDto;
import org.example.testrestsoap.dto.TestResponseDto;
import org.example.testrestsoap.service.TestService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
@ResponseStatus(HttpStatus.OK)
@Tag(name = "Counter", description = "Запросы для работы с счетчиком")
public class TestRestController {

    private final TestService testService;

    @Operation(summary = "Увеличение счетчика", description = "Увеличить счетчик на значение инкремента")
    @ApiResponse(responseCode = "200", description = "Счетчик успешно увеличен", content = @Content(mediaType = "application/json", schema = @Schema(implementation = TestResponseDto.class)))
    @ApiResponse(responseCode = "400", description = "Ошибка увеличения счетчика", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDto.class)))
    @PostMapping("/increment")
    public ResponseEntity<TestResponseDto> incrementCounter(@RequestBody TestIncrementRequestDto testRequestDto) {
        return new ResponseEntity<>(testService.incrementCounter(testRequestDto), HttpStatus.OK);
    }

    @Operation(summary = "Получение значения счетчика", description = "Получить актуальное значение счетчика")
    @ApiResponse(responseCode = "200", description = "Значение счетчика получено", content = @Content(mediaType = "application/json", schema = @Schema(implementation = TestResponseDto.class)))
    @ApiResponse(responseCode = "400", description = "Ошибка поулчения счетчика", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDto.class)))
    @GetMapping("/counter/{counterId}")
    public ResponseEntity<TestResponseDto> getCounter(@PathVariable Long counterId) {
        return new ResponseEntity<>(testService.getCounterById(counterId), HttpStatus.OK);
    }

}
