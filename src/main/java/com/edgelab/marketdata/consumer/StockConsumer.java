package com.edgelab.marketdata.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.FluxSink.OverflowStrategy;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.util.function.Consumer;

import static java.util.function.Function.identity;

@Service
@RequiredArgsConstructor
@Slf4j
public class StockConsumer {

    private final StockQuotationRepository repository;

    private FluxSink<StockQuotation> quotesSink;
    private ConnectableFlux<StockQuotation> savedQuotes;

    @PostConstruct
    private void setUpServiceCallFlux() {
        savedQuotes = Flux.<StockQuotation>create(emitter -> quotesSink = emitter.serialize(), OverflowStrategy.ERROR)
            .buffer(100, Duration.ofSeconds(10))
            .flatMap(quotes -> Mono.fromSupplier(() -> repository.save(quotes))
                .subscribeOn(Schedulers.parallel())
            )
            .flatMapIterable(identity())
            .publish();
    }

    public void save(StockQuotation stockQuotation, Consumer<StockQuotation> consumer) {
        savedQuotes.filter(quote -> quote.toString().equals(stockQuotation.toString()))
            .subscribe(consumer);
        savedQuotes.connect();
        quotesSink.next(stockQuotation);
    }

}
