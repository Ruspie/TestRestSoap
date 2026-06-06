package org.example.testrestsoap.service;

import lombok.RequiredArgsConstructor;
import org.example.testrestsoap.exception.FileException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class FileService {

    private final Path UPLOAD_CATALOG = Paths.get("uploads");

    {
        try {
            Files.createDirectories(UPLOAD_CATALOG);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при создании каталога!");
        }
    }

    public String storeFile(MultipartFile file) {
        if (file == null || file.isEmpty())
            throw new IllegalArgumentException("Файл пустой!");

        String fileName = file.getOriginalFilename();
        if (fileName == null || fileName.isEmpty())
            throw new IllegalArgumentException("Некорректное имя файла!");

        Path destinationFile = UPLOAD_CATALOG.resolve(Paths.get(fileName)).toAbsolutePath();

        try {
            Files.copy(file.getInputStream(), destinationFile, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new FileException("Ошибка сохранения файла!", e);
        }

        return fileName;
    }

    public List<String> getListFiles() {
        try (Stream<Path> pathStream = Files.walk(UPLOAD_CATALOG, 1)) {
            return pathStream
                    .filter(path -> !path.equals(this.UPLOAD_CATALOG))
                    .map(this.UPLOAD_CATALOG::relativize)
                    .map(Path::toString)
                    .toList();
        } catch (IOException e) {
            throw new FileException("Ошибка получения списка файлов в локальном хранилище!", e);
        }


    }

    public Resource getFile(String fileName) {
        try {
            Path file = UPLOAD_CATALOG.resolve(fileName);

            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() && resource.isReadable())
                return resource;
            else
                throw new FileException("Ошибка получения файла " + fileName + "!");
        } catch (MalformedURLException e) {
            throw new FileException("Ошибка получения файла " + fileName + "!", e);
        }
    }

    public void deleteFile(String fileName) {

        try {
            Path file = UPLOAD_CATALOG.resolve(fileName);

            Files.delete(file);
        } catch (IOException e) {
            throw new FileException("Ошибка удаления файла " + fileName + "!", e);
        }

    }
}
