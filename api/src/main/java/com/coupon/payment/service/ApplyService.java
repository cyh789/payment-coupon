package com.coupon.payment.service;

import com.coupon.payment.producer.CouponCreateProducer;
import com.coupon.payment.repository.AppliedUserRepository;
import com.coupon.payment.repository.CouponCountRepository;
import com.coupon.payment.repository.CouponRepository;
import org.springframework.stereotype.Service;

@Service
public class ApplyService {
    private final CouponRepository couponRepository;
    private final CouponCountRepository couponCountRepository;
    private final CouponCreateProducer couponCreateProducer;
    private final AppliedUserRepository appliedUserRepository;

    public ApplyService(CouponRepository couponRepository, CouponCountRepository couponCountRepository, CouponCreateProducer couponCreateProducer, AppliedUserRepository appliedUserRepository) {
        this.couponRepository = couponRepository;
        this.couponCountRepository = couponCountRepository;
        this.couponCreateProducer = couponCreateProducer;
        this.appliedUserRepository = appliedUserRepository;
    }

    public void flushAll() {
        couponCountRepository.flushAll();
    }

    public void apply(Long userId) {
        //1인당 쿠폰 발급 갯수 1개로 제한
        Long apply = appliedUserRepository.add(userId);
        if (apply != 1) {
            return;
        }

        long count = couponCountRepository.increment();
        if (count > 100) {
            return;
        }

        //couponRepository.save(new Coupon(userId));
        couponCreateProducer.create(userId);
    }
}
