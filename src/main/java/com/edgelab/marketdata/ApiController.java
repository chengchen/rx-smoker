package com.edgelab.marketdata;

import com.edgelab.marketdata.config.LeaderConfig;
import com.edgelab.marketdata.domain.Stock;
import com.edgelab.marketdata.domain.StockRepository;
import com.google.common.base.Stopwatch;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ApiController {

    private final StockRepository stockRepository;
    private final LeaderConfig leaderConfig;


    @GetMapping(value = "/feeds")
    public Iterable<Stock> feedQuotes() {
        Stopwatch stopwatch = Stopwatch.createStarted();

        Iterable<Stock> stocks = stockRepository.findAll();
        log.info("Elapsed: " + stopwatch.elapsed(TimeUnit.SECONDS));

        return stocks;
    }

    @GetMapping(value = "/elect")
    public void elect() {
        leaderConfig.electLeader();
    }

}
