package org.example.testrestsoap.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class TestEntity {

    private Long id;
    private String name;
    private BigDecimal counter;

}
