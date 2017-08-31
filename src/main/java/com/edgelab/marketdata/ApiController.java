package com.edgelab.marketdata;

import com.edgelab.marketdata.domain.Quote;
import com.edgelab.marketdata.domain.QuoteService;
import com.edgelab.marketdata.domain.Stock;
import com.google.common.base.Stopwatch;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequiredArgsConstructor
public class ApiController {

    private final QuoteService quoteService;

    private final Instant now = Instant.now();

    @GetMapping(value = "/feeds")
    public String feedQuotes() {
        Stopwatch stopwatch = Stopwatch.createStarted();

        Flux.range(1, 500_000)
            .map(this::buildStock)
            .flatMap(this::buildQuotes)
            .doOnNext(quote -> quoteService.write(quote.getTicker(), quote.getDate(), quote.getValue()))
            .subscribe();

        return "Elapsed: " + stopwatch.elapsed(TimeUnit.SECONDS);
    }

    private Stock buildStock(Integer i) {
        return new Stock(i, i.toString());
    }

    private Flux<Quote> buildQuotes(Stock stock) {
        return Flux.range(1, 4000)
            .map(i -> new Quote(stock.getTicker(), now.plusSeconds(i), i.doubleValue()));
    }

    @GetMapping(value = "/fetch/{ticker}")
    public List<Quote> fetchQuotes(@PathVariable String ticker) {
        return quoteService.fetchQuotes(ticker);
    }

}
