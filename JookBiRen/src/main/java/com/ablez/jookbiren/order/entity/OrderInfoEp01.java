package com.ablez.jookbiren.order.entity;

import com.ablez.jookbiren.buyer.entity.BuyerInfoEp01;
import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Entity
public class OrderInfoEp01 {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;
    private String orderNumber;
    private String platform;
    private Integer amount;
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "buyer_id")
    private BuyerInfoEp01 buyerInfoEp01;
}
