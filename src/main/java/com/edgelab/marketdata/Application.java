package com.edgelab.marketdata;

import com.edgelab.marketdata.publisher.StockPublisher;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@SpringBootApplication
public class Application {

    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }

    public static void main(String[] args) throws InterruptedException {
        ApplicationContext appContext = SpringApplication.run(Application.class, args);

        appContext.getBean(StockPublisher.class).fetchQuotes(Arrays.asList("T", "AAPL", "OHI", "MAP.MC", "SAN.MC", "ABBN", "UBSG", "BABA"));
    }

}
