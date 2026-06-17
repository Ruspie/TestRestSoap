package org.example.testrestsoap.entity.jpa;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "passports")
@ToString
public class PassportEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "passport_number", nullable = false)
    private String passportNumber;

    @OneToOne(mappedBy = "passport", fetch = FetchType.LAZY)
    private PersonEntity owner;

}
