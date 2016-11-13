package com.edgelab.marketdata.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
@RequiredArgsConstructor
@Slf4j
public class StockConsumer {

    private final StockQuotationRepository repository;

    public void save(Publisher<StockQuotation> stockQuotations) {
        Flux.from(stockQuotations)
            .buffer(100)
            .flatMap(quotes -> Mono.fromCallable(() -> repository.save(quotes))
                .subscribeOn(Schedulers.parallel()))
            .subscribe();
    }

}
