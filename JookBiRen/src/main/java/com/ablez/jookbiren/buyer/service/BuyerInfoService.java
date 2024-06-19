package com.ablez.jookbiren.buyer.service;

import com.ablez.jookbiren.buyer.dto.BuyerInfoDto.PostBuyerInfoDto;
import com.ablez.jookbiren.buyer.entity.BuyerInfo;
import com.ablez.jookbiren.buyer.repository.BuyerInfoRepository;
import com.ablez.jookbiren.order.dto.OrderInfoDto.PostOrderInfoDto;
import com.ablez.jookbiren.order.entity.OrderInfo;
import com.ablez.jookbiren.order.service.OrderInfoService;
import com.ablez.jookbiren.user.entity.UserEp01;
import com.ablez.jookbiren.user.entity.UserInfoEp01;
import com.ablez.jookbiren.user.service.UserInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
public class BuyerInfoService {
    private final BuyerInfoRepository buyerInfoRepository;
    private final UserInfoService userInfoService;
    private final OrderInfoService orderInfoService;

    public BuyerInfo insertBuyerInfo(int codeCount, PostBuyerInfoDto buyerInformation,
                                     PostOrderInfoDto orderInformation) {
        OrderInfo orderInfo = orderInfoService.makeOrderInfo(orderInformation);
//        List<UserInfoEp01> userInfos = new ArrayList<>();
        BuyerInfo buyerInfo = BuyerInfo.postBuyerInfoDtoToBuyerInfo(buyerInformation);
        buyerInfo.addOrderInfo(orderInfo);
        for (int count = 0; count < codeCount; count++) {
            UserInfoEp01 userInfo = userInfoService.makeUserInfo();
            UserEp01 user = new UserEp01(userInfo.getCode());
            userInfo.setUser(user);
            user.setUserInfo(userInfo);
            buyerInfo.addUserInfo(userInfo);
        }

        return buyerInfoRepository.save(buyerInfo);
    }
}
