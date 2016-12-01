package com.edgelab.marketdata.consumer;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StockConsumer {

    private final StockQuotationRepository repository;

    public StockQuotation save(StockQuotation stockQuotation) {
        return repository.save(stockQuotation);
    }

}
