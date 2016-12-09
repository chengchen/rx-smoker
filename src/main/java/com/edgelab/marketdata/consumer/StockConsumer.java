package com.edgelab.marketdata.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.FluxSink.OverflowStrategy;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.time.Duration;

@Service
@RequiredArgsConstructor
@Slf4j
public class StockConsumer {

    private final StockQuotationRepository repository;

    private FluxSink<StockQuotation> quotesSink;

    @PostConstruct
    private void setUpServiceCallFlux() {
        Flux.<StockQuotation>create(emitter -> quotesSink = emitter.serialize(), OverflowStrategy.ERROR)
            .log("StockConsumer")
            .buffer(100, Duration.ofSeconds(10))
            .flatMap(quotes -> Mono.fromSupplier(() -> repository.save(quotes)))
            .subscribe();
    }

    public void save(StockQuotation stockQuotation) {
        quotesSink.next(stockQuotation);
    }

}
