package org.example.testrestsoap.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Объект для возврата информации по счетчику")
public class TestResponseDto {

    @Schema(description = "Id счетчика", example = "123")
    private Long id;
    @Schema(description = "Сообщение", example = "Текст сообщения")
    private String message;
    @Schema(description = "Название", example = "Тест Название")
    private String name;
    @Schema(description = "Значение счетчика", example = "123.0")
    private BigDecimal currentCounterValue;

}
