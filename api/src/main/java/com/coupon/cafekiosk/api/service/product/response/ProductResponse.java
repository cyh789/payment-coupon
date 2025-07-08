package com.coupon.cafekiosk.api.service.product.response;

import com.coupon.cafekiosk.domain.product.Product;
import com.coupon.cafekiosk.domain.product.ProductSellingStatus;
import com.coupon.cafekiosk.domain.product.ProductType;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ProductResponse {

    private Long id;
    private String productNumber;
    private ProductType type;
    private ProductSellingStatus sellingStatus;
    private String name;
    private int price;

    @Builder
    private ProductResponse(Long id, String productNumber, ProductType type, ProductSellingStatus sellingStatus, String name, int price) {
        this.id = id;
        this.productNumber = productNumber;
        this.type = type;
        this.sellingStatus = sellingStatus;
        this.name = name;
        this.price = price;
    }

    public static ProductResponse of(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .productNumber(product.getProductNumber())
                .type(product.getType())
                .sellingStatus(product.getSellingStatus())
                .name(product.getName())
                .price(product.getPrice())
                .build();
    }

    @Override
    public String toString() {
        return "ProductResponse{" +
                "id=" + id +
                ", productNumber='" + productNumber + '\'' +
                ", type=" + type +
                ", sellingStatus=" + sellingStatus +
                ", name='" + name + '\'' +
                ", price=" + price +
                '}';
    }
}