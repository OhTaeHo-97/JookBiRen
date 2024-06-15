package com.ablez.jookbiren.user.entity;

import com.ablez.jookbiren.buyer.entity.BuyerInfoEp01;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Entity
public class UserInfoEp01 {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userInfoId;
    private String code;

    @OneToOne(mappedBy = "userInfo", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private UserEp01 user;
    @ManyToOne
    @JoinColumn(name = "buyer_id")
    private BuyerInfoEp01 buyerInfoEp01;
}
