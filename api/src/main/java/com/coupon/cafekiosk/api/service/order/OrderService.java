package com.coupon.cafekiosk.api.service.order;

import com.coupon.cafekiosk.api.controller.order.request.OrderCreateRequest;
import com.coupon.cafekiosk.domain.order.Order;
import com.coupon.cafekiosk.domain.product.Product;
import com.coupon.cafekiosk.domain.order.OrderRepository;
import com.coupon.cafekiosk.domain.product.ProductRepository;
import com.coupon.cafekiosk.api.service.order.response.OrderResponse;
import com.coupon.cafekiosk.domain.product.ProductType;
import com.coupon.cafekiosk.domain.stock.Stock;
import com.coupon.cafekiosk.domain.stock.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class OrderService {

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final StockRepository stockRepository;

    public OrderResponse createOrder(OrderCreateRequest request, LocalDateTime registeredDateTme) {
        List<String> productNumbers = request.getProductNumbers();
        List<Product> products = findProductsBy(productNumbers);

        // 재고 차감 체크가 필요한 상품들 fillter
        List<String> stockProductNumbers = products.stream()
                .filter(product -> ProductType.containsStockType(product.getType()))
                .map(Product::getProductNumber)
                .collect(Collectors.toList());
        // 재고 엔티티 조회
        List<Stock> stocks = stockRepository.findAllByProductNumberIn(stockProductNumbers);
        Map<String, Stock> stockMap = stocks.stream()
                .collect(Collectors.toMap(Stock::getProductNumber, stock -> stock));
        // 상품별 counting
        Map<String, Long> productCountingMap = stockProductNumbers.stream()
                .collect(Collectors.groupingBy(product -> product, Collectors.counting()));
        // 재고 차감 시도
        for (String stockProductNumber : new HashSet<>(stockProductNumbers)) {
            Stock stock = stockMap.get(stockProductNumber);
            int quantity = productCountingMap.get(stockProductNumber).intValue();

            if (stock.isQuantityLessThan(quantity)) {
                throw new IllegalArgumentException("재고가 부족한 상품이 있습니다.");
            }
            stock.deductQuantity(quantity);
        }

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