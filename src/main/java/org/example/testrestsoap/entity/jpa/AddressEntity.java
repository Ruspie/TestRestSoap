package org.example.testrestsoap.entity.jpa;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Collection;

@Entity
@Table(name = "addresses")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class AddressEntity extends AbstractIndentifiableObject {

    @Column(name = "city", nullable = false)
    private String city;

    @OneToMany(mappedBy = "primaryAddress", fetch = FetchType.LAZY)
    private Collection<PersonEntity> tenants;

}
