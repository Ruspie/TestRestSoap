package org.example.testrestsoap.repository.impl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.Join;
import lombok.RequiredArgsConstructor;
import org.example.testrestsoap.entity.jpa.*;
import org.example.testrestsoap.repository.JpaPersonRepositoryQueryDsl;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JpaPersonRepositoryQueryDslImpl implements JpaPersonRepositoryQueryDsl {

    private final EntityManager entityManager;

    @Override
    public List<PersonEntity> findByMultiConditionsQueryDsl(String name, String namePart, Long minAge, Long maxAge, String passport, String city) {
        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(entityManager);
        QPersonEntity personEntity = QPersonEntity.personEntity;

        BooleanBuilder filters = new BooleanBuilder();

        if (name != null && !name.isEmpty())
            filters.and(personEntity.name.eq(name));

        if (namePart != null && !namePart.isEmpty())
            filters.and(personEntity.name.containsIgnoreCase(namePart.toUpperCase()));

        if (minAge != null)
            filters.and(personEntity.age.gt(minAge));

        if (maxAge != null)
            filters.and(personEntity.age.lt(maxAge));

        if (passport != null && !passport.isEmpty()) {
            filters.and(personEntity.passport.passportNumber.eq(passport));
        }

        if (city != null && !city.isEmpty()) {
            filters.and(personEntity.primaryAddress.city.eq(city));
        }

        return jpaQueryFactory.selectFrom(personEntity)
                .where(filters)
                .orderBy(personEntity.age.asc(), personEntity.name.desc())
                .fetch();
    }

    @Override
    public List<PersonEntity> findPersonsWithPaginationQueryDsl(int offset, int limit) {
        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(entityManager);
        QPersonEntity personEntity = QPersonEntity.personEntity;

        return jpaQueryFactory.selectFrom(personEntity)
                .orderBy(personEntity.id.asc())
                .offset(offset)
                .limit(limit)
                .fetch();
    }

    @Override
    public List<Tuple> getCityWithStatsByMinAverageAgeQueryDsl(Long minAverageAge) {
        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(entityManager);
        QPersonEntity personEntity = QPersonEntity.personEntity;

        return jpaQueryFactory.select(
                personEntity.primaryAddress.city,
                personEntity.count(),
                personEntity.age.avg(),
                personEntity.age.sum(),
                personEntity.age.min(),
                personEntity.age.max()
        ).from(personEntity)
                .groupBy(personEntity.primaryAddress.city)
                .having(personEntity.age.avg().gt(minAverageAge))
                .orderBy(personEntity.age.count().asc())
                .fetch();
    }

    @Override
    public Optional<PersonEntity> findPersonByPassportNumberQueryDsl(String passportNumber) {
        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(entityManager);
        QPersonEntity personEntity = QPersonEntity.personEntity;
        QPassportEntity passportEntity = QPassportEntity.passportEntity;

        return Optional.ofNullable(
                jpaQueryFactory.selectFrom(personEntity)
                        .leftJoin(personEntity.passport, passportEntity)
                        .fetchJoin()
                        .where(passportEntity.passportNumber.eq(passportNumber))
                        .fetchOne()
        );

    }

    @Override
    public List<String> findUniqueCitiesQueryDsl() {
        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(entityManager);
        QPersonEntity personEntity = QPersonEntity.personEntity;

        return jpaQueryFactory.select(personEntity.primaryAddress.city)
                .from(personEntity)
                .distinct()
                .orderBy(personEntity.primaryAddress.city.asc())
                .fetch();
    }
}
