package com.ablez.jookbiren.order.entity;

import com.ablez.jookbiren.buyer.entity.BuyerInfo;
import com.ablez.jookbiren.order.dto.OrderInfoDto.PostOrderInfoDto;
import com.ablez.jookbiren.order.utils.Platform;
import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Entity
public class OrderInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;
    private String orderNumber;
    @Enumerated(value = EnumType.STRING)
    private Platform platform;
    private Integer amount;
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "buyer_id")
    @Setter
    private BuyerInfo buyerInfo;

    public OrderInfo(String orderNumber, Platform platform, Integer amount, LocalDateTime createdAt) {
        this.orderNumber = orderNumber;
        this.platform = platform;
        this.amount = amount;
        this.createdAt = createdAt;
    }

    public static OrderInfo postOrderInfoDtoToOrderInfo(PostOrderInfoDto orderInfoDto) {
        return new OrderInfo(orderInfoDto.getOrderNumber(), Platform.findPlatform(orderInfoDto.getPlatform()),
                orderInfoDto.getAmount(), orderInfoDto.getCreatedAt());
    }
}
