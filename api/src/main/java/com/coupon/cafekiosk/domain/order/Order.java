package com.coupon.cafekiosk.domain.order;

import com.coupon.cafekiosk.domain.BaseEntity;
import com.coupon.cafekiosk.domain.orderproduct.OrderProduct;
import com.coupon.cafekiosk.domain.product.Product;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "orders")
@Entity
public class Order extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    private int totalPrice;
    private LocalDateTime registeredDateTime;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderProduct> orderProducts = new ArrayList<>();

    public Order(List<Product> products, LocalDateTime registeredDateTime) {
        this.orderStatus = OrderStatus.INIT;
        this.totalPrice = calculateTotalPrice(products);
        this.registeredDateTime = registeredDateTime;
        this.orderProducts = products.stream()
                .map(product -> new OrderProduct(this, product))
                .collect(Collectors.toList());
    }

    public Order(List<Product> products) {
        this.orderStatus = OrderStatus.INIT;
        this.totalPrice = calculateTotalPrice(products);
        this.registeredDateTime = LocalDateTime.now();
        this.orderProducts = products.stream()
                .map(product -> new OrderProduct(this, product))
                .collect(Collectors.toList());
    }

    public static Order create(List<Product> products) {
        return new Order(products);
    }

    public static Order create(List<Product> products, LocalDateTime registeredDateTme) {
        return new Order(products, registeredDateTme);
    }

    private static int calculateTotalPrice(List<Product> products) {
        return products.stream()
                .mapToInt(Product::getPrice)
                .sum();
    }
}
