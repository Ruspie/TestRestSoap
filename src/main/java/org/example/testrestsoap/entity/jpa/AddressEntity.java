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
@ToString(exclude = "tenants")
public class AddressEntity extends AbstractIndentifiableObject implements Cloneable {

    @Column(name = "city", nullable = false)
    private String city;

    @OneToMany(mappedBy = "primaryAddress", fetch = FetchType.LAZY)
    private Collection<PersonEntity> tenants;

    @Override
    public AddressEntity clone() {
        try {
            AddressEntity clone = (AddressEntity) super.clone();
            // TODO: copy mutable state here, so the clone can't change the internals of the original
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
