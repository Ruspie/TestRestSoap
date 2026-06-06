package org.example.testrestsoap.controller;

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
public class FileController {

    private final FileService fileService;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) {
        String fileName = fileService.storeFile(file);

        FileResponseDto fileResponseDto = new FileResponseDto(
                LocalDateTime.now(),
                "Успешно загружен!",
                Collections.singletonList(fileName)
        );

        return new ResponseEntity<>(fileResponseDto, HttpStatus.OK);
    }

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

    @GetMapping("/download")
    public ResponseEntity<?> getFile(@RequestParam String fileName) {
        Resource file = fileService.getFile(fileName);

        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .body(file);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteFile(@RequestParam String fileName) {
        fileService.deleteFile(fileName);

        FileResponseDto fileResponseDto = new FileResponseDto(
                LocalDateTime.now(),
                "Файл удален",
                Collections.singletonList(fileName)
        );

        return new ResponseEntity<>(fileResponseDto, HttpStatus.OK);
    }

}
