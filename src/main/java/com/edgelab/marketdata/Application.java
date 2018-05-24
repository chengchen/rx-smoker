package com.edgelab.marketdata;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import javax.xml.stream.XMLInputFactory;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    RestTemplate rest() {
        return new RestTemplate();
    }

    @Bean
    XmlMapper xmlMapper() {
        return new XmlMapper();
    }

    @Bean
    XMLInputFactory xmlInputFactory() {
        return XMLInputFactory.newFactory();
    }

}
