package com.ablez.jookbiren.order.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class OrderInfoDto {
    @Getter
    @AllArgsConstructor
    @Builder
    public static class PostOrderInfoDto {
        private String orderNumber;
        private int amount;
        private String platform;
        private LocalDateTime createdAt;
    }
}
