package com.edgelab.marketdata;

import com.edgelab.marketdata.consumer.StockConsumer;
import com.edgelab.marketdata.consumer.StockQuotationRepository;
import com.edgelab.marketdata.consumer.StockStreamingService;
import com.edgelab.marketdata.publisher.StockPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    private final StockQuotationRepository repository;

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
                emitter.send(repository.findRevisions(uuid));
            } catch (Exception e) {
                log.error("fuck me", e);
            } finally {
                emitter.complete();
            }
        });
        return emitter;
    }

}
