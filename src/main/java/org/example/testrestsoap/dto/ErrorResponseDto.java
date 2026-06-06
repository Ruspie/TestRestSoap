package org.example.testrestsoap.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Объект общей ошибки")
public class ErrorResponseDto {

    @Schema(description = "Дата время запроса")
    private LocalDateTime errorTime;
    @Schema(description = "Код ошибки", example = "400-111")
    private Integer code;
    @Schema(description = "Текст ошибки", example = "Test error message")
    private String message;

}
