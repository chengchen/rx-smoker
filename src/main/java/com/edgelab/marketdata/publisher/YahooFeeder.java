package com.edgelab.marketdata.publisher;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;

@Component
@RequiredArgsConstructor
@Slf4j
class YahooFeeder {

    private static final String YAHOO_URL = "http://ichart.finance.yahoo.com/table.csv?s=%s&amp;a=1&amp;b=1&amp;c=2012&amp;g=ds";

    private final RestTemplate restTemplate;

    List<String> getCSVQuotes(String ticker) {
        return stream(callYahoo(ticker))
            // Skip header
            .skip(1)
            .map(line -> ticker + "," + line).collect(Collectors.toList());
    }

    private String[] callYahoo(String ticker) {
        log.info("Thread[{}] Calling Yahoo for ticker: {}", Thread.currentThread().getName(), ticker);
        String quoteResource = restTemplate.getForObject(String.format(YAHOO_URL, ticker), String.class);
        log.info("Thread[{}] Got reply from Yahoo for ticker: {}", Thread.currentThread().getName(), ticker);
        return quoteResource.split("\\r?\\n");
    }

}
