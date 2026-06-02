package org.example.testrestsoap.endpoint;

import lombok.RequiredArgsConstructor;
import org.example.test.generated.GetCounterRequestDto;
import org.example.test.generated.TestIncrementRequestDto;
import org.example.test.generated.TestResponseDto;
import org.example.testrestsoap.service.TestService;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint
@RequiredArgsConstructor
public class TestSoapEndpoint {

    private final String NAMESPACE = "http://generated.test.example.org";

    private final TestService testService;

    @PayloadRoot(namespace = NAMESPACE, localPart = "TestIncrementRequestDto")
    @ResponsePayload
    public TestResponseDto incrementCounter(@RequestPayload TestIncrementRequestDto testRequestDto) {
        TestResponseDto testResponseDto;
        try {
            testResponseDto = testService.incrementCounter(testRequestDto);
        } catch (Exception e) {
            testResponseDto = new TestResponseDto();

            testResponseDto.setMessage(e.getMessage());
        }

        return testResponseDto;
    }

    @PayloadRoot(namespace = NAMESPACE, localPart = "GetCounterRequestDto")
    @ResponsePayload
    public TestResponseDto getCounter(@RequestPayload GetCounterRequestDto getCounterRequestDto) {
        try {
            return testService.getCounterById(getCounterRequestDto.getId());
        } catch (Exception e) {
            TestResponseDto testResponseDto = new TestResponseDto();

            testResponseDto.setMessage(e.getMessage());

            return testResponseDto;
        }
    }


}
