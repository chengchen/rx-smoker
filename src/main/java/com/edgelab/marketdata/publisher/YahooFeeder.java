package com.edgelab.marketdata.publisher;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

@Component
@RequiredArgsConstructor
@Slf4j
class YahooFeeder {

    private final YahooClient client;

    List<String> getCSVQuotes(String ticker) {
        return stream(callYahoo(ticker))
            // Skip header
            .skip(1)
            .map(line -> ticker + "," + line).collect(toList());
    }

    private String[] callYahoo(String ticker) {
        log.info("Thread[{}] Calling Yahoo for ticker: {}", Thread.currentThread().getName(), ticker);
        String quoteResource = client.getQuotes(ticker, 1, 1, 2012, "ds");
        log.info("Thread[{}] Got reply from Yahoo for ticker: {}", Thread.currentThread().getName(), ticker);
        return quoteResource.split("\\r?\\n");
    }

    @FeignClient(name = "yahoo", url = "http://ichart.finance.yahoo.com")
    interface YahooClient {

        @RequestMapping(value = "/table.csv")
        String getQuotes(@RequestParam("s") String ticker,
                         @RequestParam("a") int a,
                         @RequestParam("b") int b,
                         @RequestParam("c") int c,
                         @RequestParam("g") String g);

    }

}
