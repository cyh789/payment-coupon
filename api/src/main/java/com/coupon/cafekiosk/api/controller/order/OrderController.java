package com.coupon.cafekiosk.api.controller.order;

import com.coupon.cafekiosk.api.controller.order.request.OrderCreateRequest;
import com.coupon.cafekiosk.api.service.order.response.OrderResponse;
import com.coupon.cafekiosk.api.service.order.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@RestController
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/api/v1/orders/new")
    public OrderResponse createOrder(@RequestBody OrderCreateRequest request) {
        LocalDateTime registeredDateTme = LocalDateTime.now();
        return orderService.createOrder(request, registeredDateTme);
    }
}
