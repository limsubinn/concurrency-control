package com.example.stock.facade;

import com.example.stock.service.StockService;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class RedissonLockStockFacade {

    private final RedissonClient redissonClient;
    private final StockService stockService;

    public void decrease(Long id, Long quantity) {
        // RedissonClient를 이용해서 lock 객체를 가져온다.
        RLock lock = redissonClient.getLock(id.toString());

        try {
            // lock 획득
            boolean available = lock.tryLock(10, 1, TimeUnit.SECONDS); // 획득 시간, 점유 시간

            // lock 획득 실패
            if (!available) {
                System.out.println("lock 획득 실패");
                return;
            }

            // 재고 감소
            stockService.decrease(id, quantity);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            // lock 해제
            lock.unlock();
        }
    }
}
