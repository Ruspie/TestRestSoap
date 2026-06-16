package org.example.testrestsoap;

import lombok.extern.slf4j.Slf4j;
import org.example.testrestsoap.service.TestService;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@DisplayName("Первый класс тестовый")
public class FirstTest {

    TestService testService;

    @BeforeAll
    static void initAll() {
        log.info("initAll ПЕРЕД ВСЕМИ ТЕСТАМИ");
    }

    @BeforeEach
    void init() {
        testService = new TestService(null);
        log.info("init ПЕРЕД КАЖДЫМ ТЕСТОМ");
    }

    @DisplayName("Первый тест")
    @Test
    void firstTest() {
        log.info("firstTest");
        Integer expectedResult = 4;

        Integer result = testService.sum(2, 2);

        assertEquals(expectedResult, result);
    }

    @DisplayName("Второй тест")
    @Test
    @Disabled
    void secondTest() {
        log.info("secondTest");
        Integer expectedResult = 4;

        Integer result = testService.sum(2, 2);

        assertEquals(expectedResult, result);
    }

    @RepeatedTest(value = 3, name = "Повторение {currentRepetition} из {totalRepetitions}")
    @DisplayName("Повторяющийся тест")
    void repeatableTest(){
        log.info("repeatableTest");
    }

    @AfterEach
    void down() {
        log.info("down ПОСЛЕ КАЖДОГО ТЕСТА");
    }

    @AfterAll
    static void downAll() {
        log.info("downAll ПОСЛЕ ВСЕХ ТЕСТОВ");
    }




}
