package com.coupon.cafekiosk.api.service.order;

import com.coupon.cafekiosk.api.controller.order.request.OrderCreateRequest;
import com.coupon.cafekiosk.domain.product.Product;
import com.coupon.cafekiosk.domain.product.ProductType;
import com.coupon.cafekiosk.domain.orderproduct.OrderProductRepository;
import com.coupon.cafekiosk.domain.order.OrderRepository;
import com.coupon.cafekiosk.domain.product.ProductRepository;
import com.coupon.cafekiosk.api.service.order.response.OrderResponse;
import com.coupon.cafekiosk.domain.stock.Stock;
import com.coupon.cafekiosk.domain.stock.StockRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static com.coupon.cafekiosk.domain.product.ProductSellingStatus.*;
import static com.coupon.cafekiosk.domain.product.ProductType.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.tuple;

@ActiveProfiles("test")
@Transactional
@SpringBootTest
class OrderServiceTest {
    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderProductRepository orderProductRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private StockRepository stockRepository;

//    @AfterEach
//    void tearDown() {
//        orderProductRepository.deleteAllInBatch();
//        productRepository.deleteAllInBatch();
//        orderRepository.deleteAllInBatch();
//    }

    @Test
    @DisplayName("주문 번호 리스트를 받아 주문을 생성한다.")
    void createOrder() {
        // given
        LocalDateTime registeredDateTime = LocalDateTime.now();

        Product product1 = createProduct(HANDMADE, "004", 1000);
        Product product2 = createProduct(HANDMADE, "005", 3000);
        Product product3 = createProduct(HANDMADE, "006", 5000);

        productRepository.saveAll(List.of(product1, product2, product3));

        OrderCreateRequest request = OrderCreateRequest.builder()
                .productNumbers(List.of("004", "005"))
                .build();

        // when
        OrderResponse orderResponse = orderService.createOrder(request, registeredDateTime);

        // then
        assertThat(orderResponse.getId()).isNotNull();
        assertThat(orderResponse)
                .extracting("registeredDateTime", "totalPrice")
                .contains(registeredDateTime, 4000);

        assertThat(orderResponse.getProducts()).hasSize(2)
                .extracting("productNumber", "price")
                .containsExactlyInAnyOrder(
                        tuple("004", 1000),
                        tuple("005", 3000)
                );
    }

    @Test
    @DisplayName("주문 번호 리스트를 받아 주문을 생성한다. 주문 번호가 중복인 경우, 다건 조회 및 금액 합계를 응답한다.")
    void createOrder2() {
        // given
        LocalDateTime registeredDateTime = LocalDateTime.now();

        Product product1 = createProduct(HANDMADE, "004", 1000);
        Product product2 = createProduct(HANDMADE, "005", 3000);
        Product product3 = createProduct(HANDMADE, "006", 5000);

        productRepository.saveAll(List.of(product1, product2, product3));

        OrderCreateRequest request = OrderCreateRequest.builder()
                .productNumbers(List.of("004", "004"))
                .build();

        // when
        OrderResponse orderResponse = orderService.createOrder(request, registeredDateTime);

        // then
        assertThat(orderResponse.getId()).isNotNull();
        assertThat(orderResponse)
                .extracting("registeredDateTime", "totalPrice")
                .contains(registeredDateTime, 2000);

        assertThat(orderResponse.getProducts()).hasSize(2)
                .extracting("productNumber", "price")
                .containsExactlyInAnyOrder(
                        tuple("004", 1000),
                        tuple("004", 1000)
                );
    }

    private Product createProduct(ProductType type, String productNumber, int price) {
        return Product.builder()
                .type(type)
                .productNumber(productNumber)
                .price(price)
                .sellingStatus(SELLING)
                .name("메뉴 이름") // 이름이 같아도 상관없음.
                .build();
    }

    @Test
    @DisplayName("재고와 관련된 상품이 포함되어 있는 주문번호 리스트를 받아 주문을 생성한다.")
    void createOrderWithStock() {
        // given
        LocalDateTime registeredDateTime = LocalDateTime.now();

        Product product1 = createProduct(BOTTLE, "004", 1000);
        Product product2 = createProduct(BAKERY, "005", 3000);
        Product product3 = createProduct(HANDMADE, "006", 5000);
        productRepository.saveAll(List.of(product1, product2, product3));

        Stock stock1 = Stock.create("004", 3);
        Stock stock2 = Stock.create("005", 2);
        stockRepository.saveAll(List.of(stock1, stock2));

        OrderCreateRequest request = OrderCreateRequest.builder()
                .productNumbers(List.of("004", "004", "005", "005", "006"))
                .build();

        // when
        OrderResponse orderResponse = orderService.createOrder(request, registeredDateTime);

        // then
        assertThat(orderResponse.getId()).isNotNull();
        assertThat(orderResponse)
                .extracting("registeredDateTime", "totalPrice")
                .contains(registeredDateTime, 13000);
        assertThat(orderResponse.getProducts()).hasSize(5)
                .extracting("productNumber", "price")
                .containsExactlyInAnyOrder(
                        tuple("004", 1000),
                        tuple("004", 1000),
                        tuple("005", 3000),
                        tuple("005", 3000),
                        tuple("006", 5000)
                );

        List<Stock> stocks = stockRepository.findAll();
        assertThat(stocks).hasSize(2)
                .extracting("productNumber", "quantity")
                .containsExactlyInAnyOrder(
                        tuple("004", 1),
                        tuple("005", 0)
                );
    }










}