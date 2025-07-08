package com.coupon.cafekiosk.api.service.order;

import com.coupon.cafekiosk.api.controller.order.request.OrderCreateRequest;
import com.coupon.cafekiosk.domain.order.Order;
import com.coupon.cafekiosk.domain.product.Product;
import com.coupon.cafekiosk.domain.order.OrderRepository;
import com.coupon.cafekiosk.domain.product.ProductRepository;
import com.coupon.cafekiosk.api.service.order.response.OrderResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
        List<Product> products = findProductsBy(productNumbers);

        Order order = Order.create(products, registeredDateTme);
        Order savedOrder = orderRepository.save(order);
        return OrderResponse.of(savedOrder);
    }

    private List<Product> findProductsBy(List<String> productNumbers) {
        List<Product> products = productRepository.findAllByProductNumberIn(productNumbers);

        //Order Request에 중복 건이 있는지 체크
        //1. 상품정보를 productMap에 넣어놓는다
        Map<String, Product> productMap = products.stream()
                .collect(Collectors.toMap(Product::getProductNumber, p -> p));
        //2. request의 productNumbers를 이용해 상품정보에서 매핑하여 List에 응답 값 재할당
        return productNumbers.stream()
                .map(productMap::get)
                .collect(Collectors.toList());
    }

}