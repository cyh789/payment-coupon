package com.coupon.cafekiosk.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public enum ProductSellingStatus {    // 판매 상태

    SELLING("판매 중"),
    HOLD("판매 보류"),
    STOP_SELLING("판매 중지");

    private final String text;

    public static List<ProductSellingStatus> forDisplay() {
        return List.of(SELLING, HOLD);
    }
}