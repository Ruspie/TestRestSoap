package org.example.testrestsoap.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
@Schema(description = "Объект для ответа на запрос по файлам")
public class FileResponseDto {

    @Schema(description = "Время запроса", type = "string", format = "local-date-time", example = "2026-06-09T22:31:09.1155959")
    private LocalDateTime timestamp;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(description = "Сообщение", example = "Успешно обработано")
    private String message;
    @Schema(description = "Список файлов", example = "photo_2026-03-20_00-21-11.jpg, photo_2026-03-20_00-21-11.jpg")
    private List<String> fileNameList;

}
