package com.ablez.jookbiren.buyer.repository;

import static com.ablez.jookbiren.buyer.entity.QBuyerInfo.buyerInfo;
import static com.ablez.jookbiren.order.entity.QOrderInfo.orderInfo;

import com.ablez.jookbiren.buyer.entity.BuyerInfo;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Optional;
import javax.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class BuyerInfoRepository {
    private final EntityManager em;
    private JPAQueryFactory queryFactory;

    public BuyerInfoRepository(EntityManager em) {
        this.em = em;
        this.queryFactory = new JPAQueryFactory(this.em);
    }

    public Optional<BuyerInfo> findByPhone(String phone) {
        BuyerInfo result = queryFactory.selectFrom(buyerInfo)
                .innerJoin(buyerInfo.orderInfos, orderInfo)
                .where(buyerInfo.phone.eq(phone))
                .distinct()
                .fetchOne();

        return Optional.ofNullable(result);
    }
}
