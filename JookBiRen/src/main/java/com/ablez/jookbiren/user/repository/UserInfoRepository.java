package com.ablez.jookbiren.user.repository;

import static com.ablez.jookbiren.buyer.entity.QBuyerInfo.*;
import static com.ablez.jookbiren.order.entity.QOrderInfo.*;
import static com.ablez.jookbiren.user.entity.QUserEp01.userEp01;
import static com.ablez.jookbiren.user.entity.QUserInfoEp01.userInfoEp01;

import com.ablez.jookbiren.buyer.entity.QBuyerInfo;
import com.ablez.jookbiren.order.entity.QOrderInfo;
import com.ablez.jookbiren.user.entity.UserInfoEp01;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class UserInfoRepository {
    private final EntityManager em;
    private JPAQueryFactory queryFactory;

    public UserInfoRepository(EntityManager em) {
        this.em = em;
        this.queryFactory = new JPAQueryFactory(this.em);
    }

    public Optional<UserInfoEp01> findByCode(String code) {
        UserInfoEp01 userInfo = queryFactory
                .selectFrom(userInfoEp01)
                .innerJoin(userInfoEp01.user, userEp01)
                .where(userInfoEp01.code.eq(code))
                .distinct()
                .fetchOne();
        return Optional.ofNullable(userInfo);
    }

    public List<UserInfoEp01> findAll() {
        return queryFactory
                .selectFrom(userInfoEp01)
                .innerJoin(userInfoEp01.orderInfo, orderInfo).fetchJoin()
                .innerJoin(orderInfo.buyerInfo, buyerInfo).fetchJoin()
                .orderBy(userInfoEp01.userInfoId.desc())
                .distinct()
                .fetch();
    }
}
