package com.ablez.jookbiren.buyer.service;

import com.ablez.jookbiren.buyer.dto.BuyerInfoDto.PostBuyerInfoDto;
import com.ablez.jookbiren.buyer.entity.BuyerInfo;
import com.ablez.jookbiren.buyer.repository.BuyerInfoJpaRepository;
import com.ablez.jookbiren.buyer.repository.BuyerInfoRepository;
import com.ablez.jookbiren.order.dto.OrderInfoDto.PostOrderInfoDto;
import com.ablez.jookbiren.order.entity.OrderInfo;
import com.ablez.jookbiren.order.service.OrderInfoService;
import com.ablez.jookbiren.user.entity.UserEp01;
import com.ablez.jookbiren.user.entity.UserInfoEp01;
import com.ablez.jookbiren.user.service.UserInfoService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
public class BuyerInfoService {
    private final BuyerInfoJpaRepository buyerInfoJpaRepository;
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
            orderInfo.addUserInfo(userInfo);
        }

        return buyerInfoJpaRepository.save(buyerInfo);
    }

    public void insertBuyerInfoToExistedBuyer(int codeCount, PostOrderInfoDto orderInfoDto, BuyerInfo buyerInfo) {
        OrderInfo orderInfo = orderInfoService.insertOrderInfo(orderInfoDto, buyerInfo);
        for (int count = 0; count < codeCount; count++) {
            UserInfoEp01 userInfo = userInfoService.makeUserInfo();
            UserEp01 user = new UserEp01(userInfo.getCode());
            userInfo.setUser(user);
            user.setUserInfo(userInfo);
            orderInfo.addUserInfo(userInfo);
        }
    }

    // 핸드폰 번호를 통해 구매자 정보 찾기
    public Optional<BuyerInfo> findByPhone(String phone) {
        // 같이 얘기해봐야 할 것
        //  - 핸드폰 번호가 바뀌었을 경우?
        //  - 이름, 닉네임, 주소 등이 변경될 수도 있을텐데 이럴 떄는 업데이트를 할 것인가?

        return buyerInfoRepository.findByPhone(phone);
    }
}
