package com.ablez.jookbiren.order.utils;

import com.ablez.jookbiren.exception.BusinessLogicException;
import com.ablez.jookbiren.exception.ExceptionCode;
import java.util.Arrays;
import lombok.Getter;

public enum Platform {
    NAVER("네이버"),
    TUMBLBUG("텀블벅");

    @Getter
    private String platform;

    Platform(String platform) {
        this.platform = platform;
    }

    public static Platform findPlatform(String platform) {
        return Arrays.stream(Platform.values()).filter(platformName -> platformName.getPlatform().equals(platform))
                .findFirst().orElseThrow(() -> new BusinessLogicException(ExceptionCode.PLATFORM_NOT_FOUND));
    }
}
