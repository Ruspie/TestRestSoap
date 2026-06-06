package org.example.testrestsoap.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ErrorResponseDto {

    private LocalDateTime errorTime;
    private Integer code;
    private String message;

}
