package com.example.stock.facade;

import com.example.stock.repository.RedisLockRepository;
import com.example.stock.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LettuceLockStockFacade {

    private final RedisLockRepository redisLockRepository;
    private final StockService stockService;

    public void decrease(Long id, Long quantity) throws InterruptedException {
        while (!redisLockRepository.lock(id)) {
            // lock 획득 실패 시 재시도
            Thread.sleep(100);
        }

        try {
            // 재고 감소
            stockService.decrease(id, quantity);
        } finally {
            // lock 해제
            redisLockRepository.unlock(id);
        }
    }
}
