package com.coupon.cafekiosk.service;

import com.coupon.cafekiosk.controller.OrderCreateRequest;
import com.coupon.cafekiosk.domain.Order;
import com.coupon.cafekiosk.domain.Product;
import com.coupon.cafekiosk.repository.OrderRepository;
import com.coupon.cafekiosk.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class OrderService {

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    public OrderResponse createOrder(OrderCreateRequest request) {
        List<String> productNumbers = request.getProductNumbers();
        List<Product> products = productRepository.findAllByProductNumberIn(productNumbers);
        Order order = Order.create(products);
        return OrderResponse.of(order);
    }

    public OrderResponse createOrder(OrderCreateRequest request, LocalDateTime registeredDateTme) {
        List<String> productNumbers = request.getProductNumbers();
        List<Product> products = productRepository.findAllByProductNumberIn(productNumbers);
        Order order = Order.create(products, registeredDateTme);
        Order savedOrder = orderRepository.save(order);
        return OrderResponse.of(savedOrder);
    }

}