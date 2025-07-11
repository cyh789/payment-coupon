package com.coupon.cafekiosk.domain.product;

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

    /**
     * select * from product
     * where product_number in ('1', '2')
     */
    List<Product> findAllByProductNumberIn(List<String> productNumbers);
}
