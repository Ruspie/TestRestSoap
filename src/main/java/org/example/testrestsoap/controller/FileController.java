package org.example.testrestsoap.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/test/file")
@RequiredArgsConstructor
@Tag(name = "File", description = "Запросы для работы с файлами")
public class FileController {

    private final FileService fileService;

    @Operation(summary = "Загрузить файл в хранилище", description = "Загрузка на сервер файла пользователя")
    @ApiResponse(responseCode = "200", description = "Файл успешно загружен", content = @Content(mediaType = "application/json", schema = @Schema(implementation = FileResponseDto.class)))
    @ApiResponse(responseCode = "400", description = "Ошибка загрузки файла", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDto.class)))
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadFile(
            @Parameter(description = "Переменная файл", example = "photo_2026-03-20_00-21-11.jpg")
            @RequestParam("file") MultipartFile file
    ) {
        String fileName = fileService.storeFile(file);

        FileResponseDto fileResponseDto = new FileResponseDto(
                LocalDateTime.now(),
                "Успешно загружен!",
                Collections.singletonList(fileName)
        );

        return new ResponseEntity<>(fileResponseDto, HttpStatus.OK);
    }

    @Operation(summary = "Получение информации о всех файлах в хранилище", description = "Получение файлов")
    @ApiResponse(responseCode = "200", description = "Данные получены", content = @Content(mediaType = "application/json", schema = @Schema(implementation = FileResponseDto.class)))
    @ApiResponse(responseCode = "400", description = "Ошибка получения данных", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDto.class)))
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

    @Operation(summary = "Получение файла из хралища", description = "Загрузка файла с сервера")
    @ApiResponse(responseCode = "200", description = "Данные получены", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Resource.class)))
    @ApiResponse(responseCode = "400", description = "Ошибка получения данных", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDto.class)))
    @GetMapping("/download")
    public ResponseEntity<?> getFile(
            @Parameter(description = "Переменная имени файла", example = "photo_2026-03-20_00-21-11.jpg")
            @RequestParam String fileName
    ) {
        Resource file = fileService.getFile(fileName);

        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .body(file);
    }

    @Operation(summary = "Удаление файла из хралища", description = "Удаление файла с сервера")
    @ApiResponse(responseCode = "200", description = "Файл удален", content = @Content(mediaType = "application/json", schema = @Schema(implementation = FileResponseDto.class)))
    @ApiResponse(responseCode = "400", description = "Ошибка удаления файла", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDto.class)))
    @DeleteMapping
    public ResponseEntity<?> deleteFile(
            @Parameter(description = "Переменная имени файла", example = "photo_2026-03-20_00-21-11.jpg")
            @RequestParam String fileName
    ) {
        fileService.deleteFile(fileName);

        FileResponseDto fileResponseDto = new FileResponseDto(
                LocalDateTime.now(),
                "Файл удален",
                Collections.singletonList(fileName)
        );

        return new ResponseEntity<>(fileResponseDto, HttpStatus.OK);
    }

}
