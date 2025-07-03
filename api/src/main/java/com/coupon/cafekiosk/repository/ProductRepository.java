package com.coupon.cafekiosk.repository;

import com.coupon.cafekiosk.domain.Product;
import com.coupon.cafekiosk.domain.ProductSellingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    /**
     * select * from product
     * where selling_type in ('SELLING', 'HOLD')
     */
    List<Product> findAllBySellingStatusIn(List<ProductSellingStatus> productSellingStatuses);
}
