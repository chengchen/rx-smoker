package com.edgelab.marketdata.publisher;

import com.edgelab.marketdata.domain.Stock;
import com.edgelab.marketdata.domain.StockRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class StockService {

    private final StockRepository stockRepository;
    private final IssuerService issuerService;

    @Transactional
    public Mono<Stock> createStock() {
        Stock stock = new Stock();
        stock.setName("Stock");
        stockRepository.save(stock);

        issuerService.createIssuer(stock);
        stockRepository.save(stock);

        return Mono.just(stock);
    }

}
