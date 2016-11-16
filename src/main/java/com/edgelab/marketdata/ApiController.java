package com.edgelab.marketdata;

import com.edgelab.marketdata.consumer.StockConsumer;
import com.edgelab.marketdata.publisher.StockPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ApiController {

    private final StockPublisher stockPublisher;
    private final StockConsumer stockConsumer;

    @GetMapping("/feeds")
    public ResponseBodyEmitter fetchQuotes() throws IOException {
        ResponseBodyEmitter emitter = new ResponseBodyEmitter();
        List<String> tickers = Arrays.asList("T", "AAPL", "OHI", "MAP.MC", "SAN.MC", "ABBN", "UBSG", "BABA");

        Flux.from(stockPublisher.fetchQuotes(tickers))
            .flatMap(stockQuotation -> Mono.fromRunnable(() -> {
                try {
                    emitter.send(stockConsumer.save(stockQuotation), MediaType.APPLICATION_JSON);
                } catch (Exception e) {
                    log.error("fuck me", e);
                }
            }).subscribeOn(Schedulers.parallel()))
            .doOnTerminate(emitter::complete)
            .subscribe();

        return emitter;
    }

}
