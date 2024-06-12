package com.ablez.jookbiren.user.service;

import com.ablez.jookbiren.exception.BusinessLogicException;
import com.ablez.jookbiren.exception.ExceptionCode;
import com.ablez.jookbiren.user.entity.UserInfoEp01;
import com.ablez.jookbiren.user.repository.UserInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
public class UserInfoService {
    private final UserInfoRepository userInfoRepository;

    public UserInfoEp01 findByCode(String code) {
        return userInfoRepository.findByCode(code)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.USER_INFO_NOT_FOUND));
    }
}
