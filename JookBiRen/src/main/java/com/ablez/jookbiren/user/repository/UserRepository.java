package com.ablez.jookbiren.user.repository;

import static com.ablez.jookbiren.user.entity.QUserEp01.userEp01;

import com.ablez.jookbiren.user.entity.UserEp01;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public class UserRepository {
    private final EntityManager em;
    private JPAQueryFactory queryFactory;

    public UserRepository(EntityManager em) {
        this.em = em;
        this.queryFactory = new JPAQueryFactory(this.em);
    }

    public Optional<UserEp01> findById(long userId) {
        UserEp01 user = queryFactory
                .selectFrom(userEp01)
                .where(userEp01.userId.eq(userId))
                .fetchOne();
        return Optional.ofNullable(user);
    }

    public Optional<UserEp01> findByCode(String code) {
        UserEp01 user = queryFactory
                .selectFrom(userEp01)
                .where(userEp01.code.eq(code))
                .fetchOne();
        return Optional.ofNullable(user);
    }
}
