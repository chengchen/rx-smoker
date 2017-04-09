package com.edgelab.marketdata;

import com.edgelab.marketdata.consumer.StockConsumer;
import com.edgelab.marketdata.consumer.StockStreamingService;
import com.edgelab.marketdata.inmem.CachedQuoteIndexRepository;
import com.edgelab.marketdata.inmem.CachedQuoteRepository;
import com.edgelab.marketdata.inmem.Quote;
import com.edgelab.marketdata.inmem.QuoteIndex;
import com.edgelab.marketdata.publisher.StockPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;
import reactor.core.Disposable;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ApiController {

    private final StockPublisher stockPublisher;

    private final StockConsumer stockConsumer;

    private final StockStreamingService stockStreamingService;

    private final CachedQuoteRepository quoteRepository;

    private final CachedQuoteIndexRepository quoteIndexRepository;

    @GetMapping(value = "/feeds")
    public ResponseBodyEmitter fetchQuotes() throws IOException {
        ResponseBodyEmitter emitter = new ResponseBodyEmitter();
        List<String> tickers = Arrays.asList("AAPL", "ABBN", "UBSG", "BABA");

        Disposable consumerCancellation = stockConsumer.subscribe(stockQuotation -> {
            try {
                emitter.send(stockQuotation);
            } catch (Exception e) {
                log.error("fuck me", e);
            }
        });

        stockPublisher.fetchQuotes(tickers)
            .log("StockPublisher")
            .flatMap(stockQuotation -> Mono.fromRunnable(() -> stockConsumer.save(stockQuotation)))
            .doOnComplete(consumerCancellation::dispose)
            .doAfterTerminate(emitter::complete)
            .subscribe();

        return emitter;
    }

    @GetMapping("/stockQuotations/{uuid}")
    public ResponseBodyEmitter findQuote(@PathVariable UUID uuid) {
        ResponseBodyEmitter emitter = new ResponseBodyEmitter();

        log.info("Fetching for real from db");
        stockStreamingService.fetch(uuid, stock -> {
            try {
                emitter.send(stock);
            } catch (Exception e) {
                log.error("Error fetch quote", e);
            } finally {
                emitter.complete();
            }
        });
        return emitter;
    }

    @PostMapping("/save")
    public void save() {
        Quote q1 = new Quote("1", "Jimmy");
        Quote q2 = new Quote("2", "David");
        quoteRepository.save(q1);
        quoteRepository.save(q2);

        QuoteIndex qi1 = new QuoteIndex("123", q1);
        QuoteIndex qi2 = new QuoteIndex("456", q2);
        QuoteIndex qi3 = new QuoteIndex("789", q1);
        quoteIndexRepository.save(qi1);
        quoteIndexRepository.save(qi2);
        quoteIndexRepository.save(qi3);
    }

    @GetMapping("quotes/{id}")
    public Quote find(@PathVariable String id) {
        return quoteIndexRepository.findOne(id).getQ();
    }

}
