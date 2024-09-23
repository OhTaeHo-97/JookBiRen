package com.ablez.jookbiren.buyer.entity;

import com.ablez.jookbiren.buyer.dto.BuyerInfoDto.PostBuyerInfoDto;
import com.ablez.jookbiren.order.entity.OrderInfo;
import com.ablez.jookbiren.order.utils.Platform;
import com.ablez.jookbiren.user.entity.UserInfoEp01;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Entity
public class BuyerInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long buyerId;
    private String phone;
    @Setter
    private String name;
    @Setter
    private String naverNickname;
    @Setter
    private String tumblbugNickname;
    @Setter
    private String address;

    @OneToMany(mappedBy = "buyerInfo", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OrderInfo> orderInfos = new ArrayList<>();

    public BuyerInfo(String phone, String name, String naverNickname, String tumblbugNickname, String address) {
        this.phone = phone;
        this.name = name;
        this.naverNickname = naverNickname;
        this.tumblbugNickname = tumblbugNickname;
        this.address = address;
    }

    public static BuyerInfo postBuyerInfoDtoToBuyerInfo(PostBuyerInfoDto buyerInfoDto) {
        if (Platform.findPlatform(buyerInfoDto.getPlatform()) == Platform.NAVER) {
            return new BuyerInfo(buyerInfoDto.getPhone(), buyerInfoDto.getName(), buyerInfoDto.getNickname(), null,
                    buyerInfoDto.getAddress());
        } else {
            return new BuyerInfo(buyerInfoDto.getPhone(), buyerInfoDto.getName(), null, buyerInfoDto.getNickname(),
                    buyerInfoDto.getAddress());
        }
    }

    public void addOrderInfo(OrderInfo orderInfo) {
        this.orderInfos.add(orderInfo);
        orderInfo.setBuyerInfo(this);
    }

//    public void addUserInfo(UserInfoEp01 userInfo) {
//        this.userInfos.add(userInfo);
//        userInfo.setBuyerInfo(this);
//    }
}
