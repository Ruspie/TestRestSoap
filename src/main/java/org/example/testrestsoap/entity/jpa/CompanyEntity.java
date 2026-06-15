package org.example.testrestsoap.entity.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;

@Entity
@Table(name = "companies")
@Getter
@Setter
public class CompanyEntity extends AbstractIdentifiableObject {

    @Column(nullable = false)
    private String title;

    @ManyToMany(mappedBy = "workingPlaces")
    private Collection<PersonEntity> workers;

}
