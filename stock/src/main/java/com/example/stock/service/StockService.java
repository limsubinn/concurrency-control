package com.example.stock.service;

import com.example.stock.domain.Stock;
import com.example.stock.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StockService {

    private final StockRepository stockRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW) // 부모의 트랜잭션과 별도로 수행
    public void decrease(Long id, Long quantity) {
        // Stock 조회
        Stock stock = stockRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Stock을 조회할 수 없습니다."));

        // 재고 감소
        stock.decrease(quantity);

        // 갱신된 값 저장
        stockRepository.saveAndFlush(stock);
    }

    @Transactional
    public synchronized void synchronizedDecrease(Long id, Long quantity) {
        // Stock 조회
        Stock stock = stockRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Stock을 조회할 수 없습니다."));

        // 재고 감소
        stock.decrease(quantity);

        // 갱신된 값 저장
        stockRepository.saveAndFlush(stock);
    }
}
