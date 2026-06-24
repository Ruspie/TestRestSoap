package org.example.testrestsoap.repository.impl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.example.testrestsoap.entity.jpa.PersonEntity;
import org.example.testrestsoap.entity.jpa.QPassportEntity;
import org.example.testrestsoap.entity.jpa.QPersonEntity;
import org.example.testrestsoap.repository.JpaPersonRepositoryQueryDsl;

import java.util.List;
import java.util.Optional;

public class JpaPersonRepositoryQueryDslImpl implements JpaPersonRepositoryQueryDsl {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<PersonEntity> findByMultiConditionsQueryDsl(String name, String namePart, Long minAge, Long maxAge, String passport, String city) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);

        QPersonEntity person = QPersonEntity.personEntity;

        BooleanBuilder filter = new BooleanBuilder();

        if (name != null && !name.isEmpty()) {
            filter.and(person.name.eq(name));
        }

        if (namePart != null && !namePart.isEmpty()) {
            filter.and(person.name.containsIgnoreCase(namePart));
        }

        if (minAge != null) {
            filter.and(person.age.goe(minAge));
        }

        if (maxAge != null) {
            filter.and(person.age.loe(maxAge));
        }

        if (passport != null && !passport.isEmpty()) {
            filter.and(person.passport.passportNumber.eq(passport));
        }

        if (city != null && !city.isEmpty()) {
            filter.and(person.primaryAddress.city.eq(city));
        }

        return queryFactory.selectFrom(person)
                .where(filter)
                .orderBy(person.age.asc(), person.name.desc())
                .fetch();
    }

    @Override
    public List<PersonEntity> findPersonsWithPaginationQueryDsl(int offset, int limit) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        QPersonEntity person = QPersonEntity.personEntity;

        return queryFactory.selectFrom(person)
                .orderBy(person.id.asc())
                .offset(offset)
                .limit(limit)
                .fetch();
    }

    @Override
    public List<Tuple> getCityStatsWithHavingQueryDsl(double minAverageAge) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        QPersonEntity person = QPersonEntity.personEntity;

        return queryFactory.select(
                        person.primaryAddress.city,
                        person.count(),
                        person.age.avg(),
                        person.age.sum()
                )
                .from(person)
                .groupBy(person.primaryAddress.city)
                .having(person.age.avg().gt(minAverageAge))
                .orderBy(person.count().desc())
                .fetch();
    }

    @Override
    public Optional<PersonEntity> findByPassportNumberWithFetchJoinQueryDsl(String passportNumber) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        QPersonEntity person = QPersonEntity.personEntity;
        QPassportEntity passport = QPassportEntity.passportEntity;

        PersonEntity result = queryFactory.selectFrom(person)
                .leftJoin(person.passport, passport).fetchJoin()
                .where(passport.passportNumber.eq(passportNumber))
                .fetchOne();

        return Optional.ofNullable(result);
    }

    @Override
    public List<String> findUniqueCitiesQueryDsl() {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        QPersonEntity person = QPersonEntity.personEntity;

        return queryFactory.select(person.primaryAddress.city)
                .from(person)
                .distinct()
                .orderBy(person.primaryAddress.city.asc())
                .fetch();
    }


}
