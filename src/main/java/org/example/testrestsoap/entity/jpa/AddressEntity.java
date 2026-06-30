package org.example.testrestsoap.entity.jpa;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@ToString(exclude = "tenants")
public class AddressEntity extends AbstractIndentifiableObject {

    @Column(name = "city", nullable = false)
    private String city;

    @JsonIgnore
    @OneToMany(mappedBy = "primaryAddress", fetch = FetchType.LAZY)
    private Collection<PersonEntity> tenants;

}
