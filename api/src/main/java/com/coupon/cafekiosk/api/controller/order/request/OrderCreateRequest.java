package com.coupon.cafekiosk.api.controller.order.request;

import lombok.Builder;

import java.util.List;

public class OrderCreateRequest {

    private List<String> productNumbers;

    @Builder
    public OrderCreateRequest(List<String> productNumbers) {
        this.productNumbers = productNumbers;
    }

    public List<String> getProductNumbers() {
        return productNumbers;
    }
}