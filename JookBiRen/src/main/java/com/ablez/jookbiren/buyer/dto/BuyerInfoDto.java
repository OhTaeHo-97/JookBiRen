package com.ablez.jookbiren.buyer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class BuyerInfoDto {
    @Getter
    @AllArgsConstructor
    @Builder
    public static class PostBuyerInfoDto {
        private String phone;
        private String name;
        private String platform;
        private String nickname;
        private String address;
    }
}
