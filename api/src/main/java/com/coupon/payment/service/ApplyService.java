package com.coupon.payment.service;

import com.coupon.payment.domain.Coupon;
import com.coupon.payment.producer.CouponCreateProducer;
import com.coupon.payment.repository.CouponCountRepository;
import com.coupon.payment.repository.CouponRepository;
import org.springframework.stereotype.Service;

@Service
public class ApplyService {
    private final CouponRepository couponRepository;
    private final CouponCountRepository couponCountRepository;
    private final CouponCreateProducer couponCreateProducer;

    public ApplyService(CouponRepository couponRepository, CouponCountRepository couponCountRepository, CouponCreateProducer couponCreateProducer) {
        this.couponRepository = couponRepository;
        this.couponCountRepository = couponCountRepository;
        this.couponCreateProducer = couponCreateProducer;
    }

    public void flushAll() {
        couponCountRepository.flushAll();
    }

    public void apply(Long userId) {
        long count = couponCountRepository.increment();
        if (count > 100) {
            return;
        }

        //couponRepository.save(new Coupon(userId));
        couponCreateProducer.create(userId);
    }
}
