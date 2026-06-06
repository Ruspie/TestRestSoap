package org.example.testrestsoap.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Schema
public class TestIncrementRequestDto {

    private Long id;
    private BigDecimal incrementForCounter;

}
