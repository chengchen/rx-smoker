package com.edgelab.marketdata.domain;

import java.time.Instant;
import java.util.List;

public interface QuoteService {

    void write(String ticker, Instant date, Double value);

    List<Quote> fetchQuotes(String ticker);

}
