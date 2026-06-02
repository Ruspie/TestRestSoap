package org.example.testrestsoap.controller;

import lombok.RequiredArgsConstructor;
import org.example.test.generated.GetCounterRequestDto;
import org.example.test.generated.TestIncrementRequestDto;
import org.example.test.generated.TestResponseDto;
import org.example.testrestsoap.service.TestService;
import org.springframework.web.bind.annotation.*;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
public class TestRestController {

    private final TestService testService;

    @PostMapping("/increment")
    public TestResponseDto incrementCounter(@RequestBody TestIncrementRequestDto testRequestDto) {
        TestResponseDto testResponseDto;
        try {
            testResponseDto = testService.incrementCounter(testRequestDto);
        } catch (Exception e) {
            testResponseDto = new TestResponseDto();

            testResponseDto.setMessage(e.getMessage());
        }

        return testResponseDto;
    }

    @GetMapping("/counter/{counterId}")
    public TestResponseDto getCounter(@PathVariable Long counterId) {
        try {
            return testService.getCounterById(counterId);
        } catch (Exception e) {
            TestResponseDto testResponseDto = new TestResponseDto();

            testResponseDto.setMessage(e.getMessage());

            return testResponseDto;
        }
    }

}
