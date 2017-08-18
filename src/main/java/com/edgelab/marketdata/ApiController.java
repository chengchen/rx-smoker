package com.edgelab.marketdata;

import com.edgelab.marketdata.publisher.StockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ApiController {

    private final StockService stockService;

    @GetMapping(value = "/feeds")
    public void fetchQuotes() throws IOException {
        Flux.range(1, 100)
            .flatMap(i -> Mono.defer(stockService::createStock).subscribeOn(Schedulers.parallel()))
            .subscribe(s -> log.info("Saved {}", s));
    }

}
