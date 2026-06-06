package org.example.testrestsoap.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Сущность информация о файлах")
public class FileResponseDto {

    @Schema(description = "Время исполнения запроса", example = "1")
    private LocalDateTime timestamp;
    @Schema(description = "Сообщение о файле", example = "Файл загружен")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String message;
    @Schema(description = "Список имен файлов", example = "123, 123")
    private List<String> fileNameList;

}
