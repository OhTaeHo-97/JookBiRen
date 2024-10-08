package com.ablez.jookbiren.order.entity;

import com.ablez.jookbiren.buyer.entity.BuyerInfo;
import com.ablez.jookbiren.order.dto.OrderInfoDto.PostOrderInfoDto;
import com.ablez.jookbiren.order.utils.Platform;
import com.ablez.jookbiren.user.entity.UserInfoEp01;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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
    @OneToMany(mappedBy = "orderInfo", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<UserInfoEp01> userInfos = new ArrayList<>();

    public OrderInfo(String orderNumber, Platform platform, Integer amount, LocalDateTime createdAt) {
        this.orderNumber = orderNumber;
        this.platform = platform;
        this.amount = amount;
        this.createdAt = createdAt;
    }

    public OrderInfo(String orderNumber, Platform platform, Integer amount, LocalDateTime createdAt,
                     BuyerInfo buyerInfo) {
        this.orderNumber = orderNumber;
        this.platform = platform;
        this.amount = amount;
        this.createdAt = createdAt;
        this.buyerInfo = buyerInfo;
    }

    public static OrderInfo postOrderInfoDtoToOrderInfo(PostOrderInfoDto orderInfoDto) {
        return new OrderInfo(orderInfoDto.getOrderNumber(), Platform.findPlatform(orderInfoDto.getPlatform()),
                orderInfoDto.getAmount(), orderInfoDto.getCreatedAt());
    }

    public static OrderInfo postOrderInfoDtoToOrderInfo(PostOrderInfoDto orderInfoDto, BuyerInfo buyerInfo) {
        return new OrderInfo(orderInfoDto.getOrderNumber(), Platform.findPlatform(orderInfoDto.getPlatform()),
                orderInfoDto.getAmount(), orderInfoDto.getCreatedAt(), buyerInfo);
    }

    public void addUserInfo(UserInfoEp01 userInfo) {
        this.userInfos.add(userInfo);
        userInfo.setOrderInfo(this);
    }
}
