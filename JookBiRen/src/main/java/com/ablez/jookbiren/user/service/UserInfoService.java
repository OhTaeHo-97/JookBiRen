package com.ablez.jookbiren.user.service;

import com.ablez.jookbiren.exception.BusinessLogicException;
import com.ablez.jookbiren.exception.ExceptionCode;
import com.ablez.jookbiren.order.entity.OrderInfo;
import com.ablez.jookbiren.order.utils.Platform;
import com.ablez.jookbiren.user.dto.UserDto.UserInfoDto;
import com.ablez.jookbiren.user.entity.UserInfoEp01;
import com.ablez.jookbiren.user.repository.UserInfoJpaRepository;
import com.ablez.jookbiren.user.repository.UserInfoRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
public class UserInfoService {
    private static final int CODE_LENGTH = 6;

    private final UserInfoRepository userInfoRepository;
    private final UserInfoJpaRepository userInfoJpaRepository;

    public UserInfoEp01 findByCode(String code) {
        return userInfoRepository.findByCode(code)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.USER_INFO_NOT_FOUND));
    }

    public UserInfoEp01 insertUserInfo() {
        String code = null;
        while (true) {
            code = generateCode();
            Optional<UserInfoEp01> userInfo = userInfoRepository.findByCode(code);
            if (userInfo.isEmpty()) {
                break;
            }
        }

        return userInfoJpaRepository.save(new UserInfoEp01(code));
    }

    public UserInfoEp01 makeUserInfo() {
        String code = null;
        while (true) {
            code = generateCode();
            Optional<UserInfoEp01> userInfo = userInfoRepository.findByCode(code);
            if (userInfo.isEmpty()) {
                break;
            }
        }

        return new UserInfoEp01(code);
    }

    // 랜덤 문자열(코드) 생성
    private String generateCode() {
        String code = "EP1" + RandomStringUtils.randomAlphanumeric(CODE_LENGTH).toUpperCase();
        while (userInfoRepository.findByCode(code).isPresent()) {
            code = "EP1" + RandomStringUtils.randomAlphanumeric(CODE_LENGTH).toUpperCase();
        }

        return code;
    }

    public List<UserInfoDto> findUserInfos() {
        List<UserInfoEp01> userInfos = userInfoRepository.findAll();
        return userInfos.stream().map(this::makeUserInfoDto).collect(Collectors.toList());
    }

    private UserInfoDto makeUserInfoDto(UserInfoEp01 userInfo) {
        return UserInfoDto.builder()
                .phone(userInfo.getOrderInfo().getBuyerInfo().getPhone())
                .name(userInfo.getOrderInfo().getBuyerInfo().getName())
                .platform(userInfo.getOrderInfo().getPlatform().getPlatform())
                .orderNumber(userInfo.getOrderInfo().getOrderNumber())
                .nickname(findNickname(userInfo.getOrderInfo()))
                .code(userInfo.getCode())
                .build();
    }

    private String findNickname(OrderInfo orderInfo) {
        if(orderInfo.getPlatform() == Platform.NAVER) {
            return orderInfo.getBuyerInfo().getNaverNickname();
        }
        return orderInfo.getBuyerInfo().getTumblbugNickname();
    }
}
