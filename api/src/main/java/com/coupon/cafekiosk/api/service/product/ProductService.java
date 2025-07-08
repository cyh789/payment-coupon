package com.coupon.cafekiosk.api.service.product;

import com.coupon.cafekiosk.domain.product.Product;
import com.coupon.cafekiosk.domain.product.ProductSellingStatus;
import com.coupon.cafekiosk.domain.product.ProductRepository;
import com.coupon.cafekiosk.api.service.product.response.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ProductService {

    private final ProductRepository productRepository;


    public List<ProductResponse> getSellingProducts() {
        List<Product> products = productRepository.findAllBySellingStatusIn(ProductSellingStatus.forDisplay());

        return products.stream()
                .map(ProductResponse::of)
                .collect(Collectors.toList());
    }
}