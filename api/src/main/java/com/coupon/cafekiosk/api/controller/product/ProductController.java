package com.coupon.cafekiosk.api.controller.product;

import com.coupon.cafekiosk.api.service.product.response.ProductResponse;
import com.coupon.cafekiosk.api.service.product.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class ProductController {

    private final ProductService productService;

//    @PostMapping("/api/v1/products/new")
//    public List<ProductResponse> createProduct(@Valid @RequestBody ProductCreateRequest request) {
//        return productService.createProduct(request.toServiceRequest());
//    }

    @GetMapping("/api/v1/products/selling")
    public List<ProductResponse> getSellingProducts() {
        return productService.getSellingProducts();
    }
}