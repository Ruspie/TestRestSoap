package org.example.testrestsoap;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@DisplayName("Тестирование базовое")
public class FirstTest {

    @BeforeAll
    static void initAll() {
        log.info("Выполняется ОДИН раз перед абсолютно всеми тестами");
    }

    @BeforeEach
    void init() {
        log.info("Выполняется перед КАЖДЫМ тестовым методом");
    }

    @Test
    @DisplayName("Проверка сложения двух чисел")
    void testAddition() {
        int result = 2 + 2;
        log.info("Проверка сложения двух чисел");
        assertEquals(4, result, "2 + 2 должно быть равно 4");
    }

    @RepeatedTest(value = 3, name = "Повторение {currentRepetition} из {totalRepetitions}")
    @DisplayName("Повторяющийся тест")
    void testRepeated() {
        // Этот метод выполнится ровно 3 раза подряд
        log.info("Тест-шаблон выполняется повторно...");
    }

    @Disabled("Временно отключен, чиним баг #104")
    @Test
    void testDisabled() {
        // Этот тест будет пропущен при запуске
    }

    @AfterEach
    void tearDown() {
        log.info("Выполняется после КАЖДОГО метода");
    }

    @AfterAll
    static void tearDownAll() {
        log.info("Выполняется ОДИН раз после всех тестов");
    }

}
