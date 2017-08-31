package com.edgelab.marketdata.domain;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Point;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.influxdb.impl.InfluxDBResultMapper;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static java.lang.String.format;

@Service
public class QuoteService {

    private static final InfluxDBResultMapper RESULT_MAPPER = new InfluxDBResultMapper();

    private static final String DB_NAME = "marketdata";

    private final InfluxDB influxDB;

    public QuoteService() {
        influxDB = InfluxDBFactory.connect("http://localhost:8086");
        influxDB.createDatabase(DB_NAME);
        influxDB.setDatabase(DB_NAME);
        influxDB.enableBatch(4000, 200, TimeUnit.MILLISECONDS);
        influxDB.enableGzip();
    }

    public void write(String ticker, Instant date, Double value) {
        influxDB.write(Point.measurement("quotes")
            .time(date.getEpochSecond(), TimeUnit.SECONDS)
            .tag("ticker", ticker)
            .addField("value", value)
            .build());
    }

    public List<Quote> fetchQuotes(String ticker) {
        Query query = new Query(format("SELECT * FROM quotes where ticker = '%s'", ticker), DB_NAME);
        QueryResult result = influxDB.query(query);

        return RESULT_MAPPER.toPOJO(result, Quote.class);
    }

}
