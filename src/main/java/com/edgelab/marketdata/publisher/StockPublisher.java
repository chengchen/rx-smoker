package com.edgelab.marketdata.publisher;

import com.edgelab.marketdata.consumer.StockConsumer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.Cancellation;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
@Slf4j
public class StockPublisher {

    private final YahooFeeder feeder;

    private final CSVStockQuotationConverter converter;

    private final StockConsumer consumer;

    public Cancellation fetchQuotes(List<String> tickers) {
        return Flux.fromIterable(tickers)
            .log()
            // Get the quotes in a separate thread
            .flatMap(ticker -> Mono.fromCallable(() -> feeder.getCSVQuotes(ticker))
                .subscribeOn(Schedulers.parallel())
            )
            .map(quotes -> quotes.stream()
                .map(converter::convertHistoricalCSVToStockQuotation)
                .collect(toList())
            )
            // Save the quotes in a separate thread
            .flatMap(quotations -> Mono.fromCallable(() -> consumer.save(quotations))
                .subscribeOn(Schedulers.parallel())
            )
            .subscribe();
    }

}
