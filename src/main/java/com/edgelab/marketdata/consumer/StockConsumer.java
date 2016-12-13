package com.edgelab.marketdata.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.Cancellation;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.FluxSink.OverflowStrategy;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.util.function.Consumer;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
@Slf4j
public class StockConsumer {

    private final StockQuotationRepository repository;

    private FluxSink<StockQuotation> quotesSink;
    private Flux<StockQuotation> consumerFlux;

    @PostConstruct
    private void setUpServiceCallFlux() {
        consumerFlux = Flux.<StockQuotation>create(emitter -> quotesSink = emitter.serialize(), OverflowStrategy.ERROR)
            .log("StockConsumer")
            .buffer(100, Duration.ofSeconds(10))
            .flatMap(quotes -> Mono.fromSupplier(() -> repository.save(quotes)))
            .flatMapIterable(Function.identity());
    }

    public Cancellation subscribe(Consumer<? super StockQuotation> consumer, Runnable completeConsumer) {
        return consumerFlux.subscribe(consumer, error -> log.error("Error!", error), completeConsumer);
    }

    public void save(StockQuotation stockQuotation) {
        quotesSink.next(stockQuotation);
    }

    public void done() {
        quotesSink.complete();
    }
}
