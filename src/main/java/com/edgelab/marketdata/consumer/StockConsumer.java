package com.edgelab.marketdata.consumer;

import lombok.RequiredArgsConstructor;
import org.reactivestreams.Publisher;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
@RequiredArgsConstructor
public class StockConsumer {

    private final StockQuotationRepository repository;

    public void save(Publisher<StockQuotation> stockQuotations) {
        Flux.from(stockQuotations)
            .buffer(100)
            .flatMap(quotes -> Mono.fromCallable(() -> repository.save(quotes))
                .subscribeOn(Schedulers.parallel()))
            .subscribe();
    }

    public StockQuotation save(StockQuotation stockQuotation) {
        return repository.save(stockQuotation);
    }

}
