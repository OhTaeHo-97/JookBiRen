package com.ablez.jookbiren.buyer.entity;

import com.ablez.jookbiren.order.entity.OrderInfoEp01;
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

@NoArgsConstructor
@Getter
@Entity
public class BuyerInfoEp01 {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long buyerId;
    private String phone;
    private String name;
    private String nickname;
    private String address;

    @OneToMany(mappedBy = "buyerInfoEp01", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OrderInfoEp01> orderInfos = new ArrayList<>();
    @OneToMany(mappedBy = "buyerInfoEp01", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<UserInfoEp01> userInfos = new ArrayList<>();
}
