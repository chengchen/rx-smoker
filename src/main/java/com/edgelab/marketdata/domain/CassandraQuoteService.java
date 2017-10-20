package com.edgelab.marketdata.domain;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@Primary
public class CassandraQuoteService implements QuoteService {

    @Override
    public void write(String ticker, Instant date, Double value) {

    }

    @Override
    public List<Quote> fetchQuotes(String ticker) {
        return null;
    }

}
