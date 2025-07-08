package com.coupon.cafekiosk.domain.product;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static com.coupon.cafekiosk.domain.product.ProductSellingStatus.*;
import static com.coupon.cafekiosk.domain.product.ProductType.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@ActiveProfiles("test")
@DataJpaTest
class ProductRepositoryTest {
    @Autowired
    private ProductRepository productRepository;

    @Test
    @DisplayName("원하는 판매상태를 가진 상품들을 조회한다.")
    void findAllBySellingStatusIn() {
        // when
        List<Product> products = productRepository.findAllBySellingStatusIn(List.of(SELLING, HOLD));

        // then
        assertThat(products).hasSize(2)
                .extracting("productNumber", "name", "sellingStatus")
                .containsExactlyInAnyOrder(                                          // 여러 contains 중, 순서와 상관없이 검증을 해준다. // 순서와 상관있는 것은 containsExactly 이다.
//                .containsExactly(                                                      // 여러 contains 중, 순서와 상관없이 검증을 해준다. // 순서와 상관있는 것은 containsExactly 이다.
                        tuple("002", "카페라떼", HOLD),
                        tuple("001", "아메리카노", SELLING)
                );
    }

    @Test
    @DisplayName("상품을 직접 추가 후, 원하는 판매상태를 가진 상품들을 조회한다.")
    void findAllBySellingStatusIn2() {
        // given
        Product product1 = Product.builder()
                .productNumber("004")
                .type(HANDMADE)
                .sellingStatus(SELLING)
                .name("(event)아메리카노")
                .price(4000)
                .build();

        Product product2 = Product.builder()
                .productNumber("005")
                .type(HANDMADE)
                .sellingStatus(HOLD)
                .name("(event)카페라떼")
                .price(4500)
                .build();

        Product product3 = Product.builder()
                .productNumber("006")
                .type(HANDMADE)
                .sellingStatus(STOP_SELLING)
                .name("(event)팥빙수")
                .price(7000)
                .build();

        productRepository.saveAll(List.of(product1, product2, product3));

        // when
        List<Product> products = productRepository.findAllBySellingStatusIn(List.of(SELLING, HOLD));

        // then
        assertThat(products).hasSize(4)
                .extracting("productNumber", "name", "sellingStatus")
                .containsExactlyInAnyOrder(
                        tuple("005", "(event)카페라떼", HOLD),
                        tuple("004", "(event)아메리카노", SELLING),
                        tuple("002", "카페라떼", HOLD),
                        tuple("001", "아메리카노", SELLING)
                );
    }

    @Test
    @DisplayName("상품번호 리스트로 상품들을 조회한다.")
    void findAllByProductNumberIn() {
        // given
        Product product1 = Product.builder()
                .productNumber("004")
                .type(HANDMADE)
                .sellingStatus(SELLING)
                .name("(event)아메리카노")
                .price(4000)
                .build();

        Product product2 = Product.builder()
                .productNumber("005")
                .type(HANDMADE)
                .sellingStatus(HOLD)
                .name("(event)카페라떼")
                .price(4500)
                .build();

        Product product3 = Product.builder()
                .productNumber("006")
                .type(HANDMADE)
                .sellingStatus(STOP_SELLING)
                .name("(event)팥빙수")
                .price(7000)
                .build();

        productRepository.saveAll(List.of(product1, product2, product3));

        // when
        List<Product> products = productRepository.findAllByProductNumberIn(List.of("001", "002", "004", "005"));

        // then
        assertThat(products).hasSize(4)
                .extracting("productNumber", "name", "sellingStatus")
                .containsExactlyInAnyOrder(
                        tuple("005", "(event)카페라떼", HOLD),
                        tuple("004", "(event)아메리카노", SELLING),
                        tuple("002", "카페라떼", HOLD),
                        tuple("001", "아메리카노", SELLING)
                );
    }
}