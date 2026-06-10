package org.example.testrestsoap.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Schema(description = "Объект для возврата ошибки")
public class ErrorResponseDto {

    @Schema(description = "Время запроса", type = "string", format = "local-date-time", example = "2026-06-09T22:31:09.1155959")
    private LocalDateTime errorTime;
    @Schema(description = "Код ошибки", example = "400-122")
    private Integer code;
    @Schema(description = "Сообщение", example = "Ошибка обработки")
    private String message;

}
