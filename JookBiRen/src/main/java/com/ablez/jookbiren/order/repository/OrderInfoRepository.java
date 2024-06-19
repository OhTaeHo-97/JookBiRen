package com.ablez.jookbiren.order.repository;

import com.ablez.jookbiren.order.entity.OrderInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderInfoRepository extends JpaRepository<OrderInfo, Long> {
}
