package org.example.testrestsoap.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.testrestsoap.dto.ErrorResponseDto;
import org.example.testrestsoap.dto.FileResponseDto;
import org.example.testrestsoap.service.FileService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Tag(name = "File", description = "Запросы для работы с файлами")
@RestController
@RequestMapping("/api/test/file")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @Operation(summary = "Загрузка файла", description = "Запрос для загрузки файла по имени файла")
    @ApiResponse(responseCode = "200", description = "Успешное выполнение запроса", content = @Content(
         mediaType = "application/json", schema = @Schema(implementation = FileResponseDto.class)
    ))
    @ApiResponse(responseCode = "400", description = "Выполнение запроса с ошибкой", content = @Content(
            mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDto.class)
    ))
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadFile(@Parameter(description = "Имя файла") @RequestParam("file") MultipartFile file) {
        try {
            String fileName = fileService.storeFile(file);

            FileResponseDto fileResponseDto = new FileResponseDto(
                    LocalDateTime.now(),
                    "Успешно загружен!",
                    Collections.singletonList(fileName)
            );

            return new ResponseEntity<>(fileResponseDto, HttpStatus.OK);
        } catch (Throwable e) {
            return new ResponseEntity<>(new ErrorResponseDto(LocalDateTime.now(), 400, "Ошибка"), HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Получения списка файлов", description = "Запрос для получения списка файлов, которые хранятся на сервере")
    @ApiResponse(responseCode = "200", description = "Успешное выполнение запроса", content = @Content(
            mediaType = "application/json", schema = @Schema(implementation = FileResponseDto.class)
    ))
    @ApiResponse(responseCode = "400", description = "Выполнение запроса с ошибкой", content = @Content(
            mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDto.class)
    ))
    @GetMapping
    public ResponseEntity<?> getListFiles() {
        List<String> files = fileService.getListFiles();

        FileResponseDto fileResponseDto = new FileResponseDto(
                LocalDateTime.now(),
                null,
                files
        );

        return new ResponseEntity<>(fileResponseDto, HttpStatus.OK);
    }

    @Operation(summary = "Получение файла", description = "Запрос для получения файла с хранилища на сервере")
    @ApiResponse(responseCode = "200", description = "Успешное выполнение запроса", content = @Content(
            mediaType = "application/json", schema = @Schema(implementation = Resource.class)
    ))
    @ApiResponse(responseCode = "400", description = "Выполнение запроса с ошибкой", content = @Content(
            mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDto.class)
    ))
    @GetMapping("/download")
    public ResponseEntity<?> getFile(@Parameter(description = "Имя файла") @RequestParam String fileName) {
        Resource file = fileService.getFile(fileName);

        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .body(file);
    }

    @Operation(summary = "Удаление файла", description = "Запрос для удаления файла по имени файла")
    @ApiResponse(responseCode = "200", description = "Успешное выполнение запроса", content = @Content(
            mediaType = "application/json", schema = @Schema(implementation = FileResponseDto.class)
    ))
    @ApiResponse(responseCode = "400", description = "Выполнение запроса с ошибкой", content = @Content(
            mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDto.class)
    ))
    @DeleteMapping
    public ResponseEntity<?> deleteFile(@Parameter(description = "Имя файла") @RequestParam String fileName) {
        fileService.deleteFile(fileName);

        FileResponseDto fileResponseDto = new FileResponseDto(
                LocalDateTime.now(),
                "Файл удален",
                Collections.singletonList(fileName)
        );

        return new ResponseEntity<>(fileResponseDto, HttpStatus.OK);
    }

}
