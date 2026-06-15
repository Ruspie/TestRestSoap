package org.example.testrestsoap.entity.jpa;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "passports")
@Getter
@Setter
public class PassportEntity extends AbstractIdentifiableObject {

    @Column(name = "passport_number", nullable = false)
    private String passportNumber;

    @OneToOne(mappedBy = "passport", fetch = FetchType.EAGER)
    private PersonEntity owner;

}
