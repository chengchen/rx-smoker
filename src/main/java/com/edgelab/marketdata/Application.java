package com.edgelab.marketdata;

import com.edgelab.marketdata.consumer.StockConsumer;
import com.edgelab.marketdata.consumer.StockQuotation;
import com.edgelab.marketdata.publisher.StockPublisher;
import org.reactivestreams.Publisher;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.ApplicationContext;

import java.util.Arrays;

@SpringBootApplication
@EnableFeignClients
public class Application {

    public static void main(String[] args) throws InterruptedException {
        ApplicationContext appContext = SpringApplication.run(Application.class, args);

        Publisher<StockQuotation> quotes = appContext.getBean(StockPublisher.class)
            .fetchQuotes(Arrays.asList("T", "AAPL", "OHI", "MAP.MC", "SAN.MC", "ABBN", "UBSG", "BABA"));

        appContext.getBean(StockConsumer.class).save(quotes);
    }

}
