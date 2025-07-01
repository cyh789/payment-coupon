package com.coupon.payment.service;

import com.coupon.payment.repository.CouponCountRepository;
import com.coupon.payment.repository.CouponRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ApplyServiceTest {

    @Autowired
    private ApplyService applyService;

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private CouponCountRepository couponCountRepository;

    @Test
    public void 한번만응모() {
        applyService.apply(1L);

        long count = couponRepository.count();
        assertThat(count).isEqualTo(1L);
    }

    @Test
    public void 여러명응모() throws InterruptedException {
        applyService.flushAll();

        int threadCount = 1000;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            long userId = i;
            executorService.submit(() -> {
                try {
                    applyService.apply(userId);
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();

        //10초
        Thread.sleep(10000);

        long count = couponRepository.count();
        assertThat(count).isEqualTo(100L);
    }

    //Consumer 실행
    //docker exec -it kafka kafka-console-consumer.sh --topic coupon_create --bootstrap-server localhost:9092 --key-deserializer "org.apache.kafka.common.serialization.StringDeserializer" --value-deserializer "org.apache.kafka.common.serialization.LongDeserializer"

    //Topic 생성
    //docker exec -it kafka kafka-topics.sh --bootstrap-server localhost:9092 --create --topic coupon_create
}