package com.edgelab.marketdata.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@RequiredArgsConstructor
@Slf4j
public class StockConsumer {

    private final StockQuotationRepository repository;

    public Iterable<StockQuotation> save(Collection<StockQuotation> stockQuotations) {
        log.info("Thread[{}] saving quotations", Thread.currentThread().getName());
        return repository.save(stockQuotations);
    }

}
