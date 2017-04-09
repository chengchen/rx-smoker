package com.edgelab.marketdata.consumer;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.FluxSink.OverflowStrategy;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class StockStreamingService {

    private final StockQuotationRepository repository;

    private FluxSink<UUID> uuidSink;

    private Flux<StockQuotation> stockStream;

    @PostConstruct
    private void stockStream() {
        stockStream = Flux.<UUID>create(emitter -> uuidSink = emitter, OverflowStrategy.ERROR)
            .log("StockStream")
            .bufferTimeout(100, Duration.ofMillis(100))
            .flatMap(ids -> Mono.fromSupplier(() -> repository.findAll(ids)))
            .flatMapIterable(Function.identity())
            .publish().autoConnect();
    }

    public void fetch(UUID id, Consumer<StockQuotation> consumer) {
        Mono.from(stockStream)
            .log("SingleFetch")
            .filter(stockQuotation -> stockQuotation.getId().equals(id))
            .onTerminateDetach()
            .subscribe(consumer);

        uuidSink.next(id);
    }

}
