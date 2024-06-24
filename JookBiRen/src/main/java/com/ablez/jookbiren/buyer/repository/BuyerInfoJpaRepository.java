package com.ablez.jookbiren.buyer.repository;

import com.ablez.jookbiren.buyer.entity.BuyerInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BuyerInfoJpaRepository extends JpaRepository<BuyerInfo, Long> {
}
