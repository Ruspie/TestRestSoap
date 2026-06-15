package org.example.testrestsoap.entity.jpa;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;

@Entity
@Table(name = "addresses")
@Getter
@Setter
public class AddressEntity extends AbstractIdentifiableObject {

    @Column(nullable = false)
    private String city;

    @OneToMany(mappedBy = "primaryAddress", fetch = FetchType.LAZY)
    private Collection<PersonEntity> tenants;

}
