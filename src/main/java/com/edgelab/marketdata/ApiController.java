package com.edgelab.marketdata;

import com.edgelab.marketdata.domain.Quote;
import com.edgelab.marketdata.domain.QuoteService;
import com.edgelab.marketdata.domain.Stock;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.Instant;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ApiController {

    private final QuoteService quoteService;

    @GetMapping(value = "/feeds")
    public void feedQuotes() {
        Flux.range(1, 1000000)
            .map(this::buildStock)
            .flatMap(this::buildQuotes)
            .doOnNext(quote -> quoteService.write(quote.getTicker(), quote.getDate(), quote.getValue()))
            .subscribe();
    }

    private Stock buildStock(Integer i) {
        return new Stock(i, i.toString());
    }

    private Flux<Quote> buildQuotes(Stock stock) {
        return Flux.range(1, 4000)
            .map(i -> new Quote(stock.getTicker(), Instant.now().plusSeconds(i), Math.random()));
    }

    @GetMapping(value = "/fetch/{ticker}")
    public List<Quote> fetchQuotes(@PathVariable String ticker) {
        return quoteService.fetchQuotes(ticker);
    }

}
