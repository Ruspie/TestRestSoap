package org.example.testrestsoap.service;

import org.example.testrestsoap.exception.FileException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Тестирование FileService")
class FileServiceTest {

    private FileService fileService;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        fileService = new FileService();

        // Магия рефлексии: подменяем приватное поле UPLOAD_CATALOG в сервисе
        // на нашу безопасную временную папку, чтобы тесты не гадили в корень проекта.
        ReflectionTestUtils.setField(fileService, "UPLOAD_CATALOG", tempDir);
    }

    @AfterEach
    void tearDown() {
    }

    // ====================================================================
    // СЦЕНАРИЙ 1: Успешное сохранение файла (Смесь JUnit + Mockito)
    // ====================================================================
    @Test
    @DisplayName("Успешное сохранение корректного файла")
    void storeFile_Success() throws IOException {
        // 1. ARRANGE (Подготовка)
        // Создаем Мок для MultipartFile — нам нужно настроить его поведение
        MultipartFile multipartFile = Mockito.mock(MultipartFile.class);

        byte[] content = "Привет, я текстовый файл!".getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(content);

        // Обучаем наш мок возвращать нужные данные при вызове его методов
        Mockito.when(multipartFile.isEmpty()).thenReturn(false);
        Mockito.when(multipartFile.getOriginalFilename()).thenReturn("test-file.txt");
        Mockito.when(multipartFile.getInputStream()).thenReturn(inputStream);

        // 2. ACT (Действие)
        // Вызываем тестируемый метод реального сервиса
        String returnedFileName = fileService.storeFile(multipartFile);

        // 3. ASSERT (Проверка результата)
        assertEquals("test-file.txt", returnedFileName, "Сервис должен вернуть имя сохраненного файла");

        // Проверяем, что файл физически появился в нашей временной папке
        Path expectedFileLocation = tempDir.resolve("test-file.txt");
        assertTrue(expectedFileLocation.toFile().exists(), "Файл должен быть физически создан на диске");
    }

    // ====================================================================
    // СЦЕНАРИЙ 2: Тест валидации на пустой файл (Чистый Mockito + JUnit)
    // ====================================================================
    @Test
    @DisplayName("Выброс IllegalArgumentException, если файл пустой")
    void storeFile_ThrowsIllegalArgumentException_WhenFileIsEmpty() {
        // 1. ARRANGE (Подготовка)
        MultipartFile emptyFile = Mockito.mock(MultipartFile.class);

        // Обучаем мок притворяться пустым
        Mockito.when(emptyFile.isEmpty()).thenReturn(true);

        // 2. ACT & 3. ASSERT (Действие объединенное с проверкой ошибки)
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            fileService.storeFile(emptyFile);
        });

        assertEquals("Файл пустой!", exception.getMessage());
    }

    // ====================================================================
    // СЦЕНАРИЙ 3: Тест падения ввода-вывода (Проверка работы с FileException)
    // ====================================================================
    @Test
    @DisplayName("Выброс FileException, если сломался стрим при копировании")
    void storeFile_ThrowsFileException_WhenIOExceptionOccurs() throws IOException {
        // 1. ARRANGE (Подготовка)
        MultipartFile faultyFile = Mockito.mock(MultipartFile.class);

        Mockito.when(faultyFile.isEmpty()).thenReturn(false);
        Mockito.when(faultyFile.getOriginalFilename()).thenReturn("broken-file.jpg");

        // Ломаем мок! Говорим ему: «Как только сервис попросит твой InputStream, жестко кинь IOException»
        Mockito.when(faultyFile.getInputStream()).thenThrow(new IOException("Диск внезапно извлечен"));

        // 2. ACT & 3. ASSERT
        FileException exception = assertThrows(FileException.class, () -> {
            fileService.storeFile(faultyFile);
        });

        assertTrue(exception.getMessage().contains("Ошибка сохранения файла!"));
    }

    @Test
    void getListFiles() {
    }

    @Test
    void getFile() {
    }

    @Test
    void deleteFile() {
    }
}