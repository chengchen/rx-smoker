package com.edgelab.marketdata;

import com.edgelab.marketdata.xml.ReactiveXmlParser;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import reactor.core.publisher.Flux;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ApiController {

    private final RestTemplate rest;
    private final ReactiveXmlParser xmlParser;

    @GetMapping("/hello")
    public String hello() {
        log.info("Calling Hello");

        String resp = rest.getForObject("http://localhost:8080/world", String.class);
        return "Hello " + resp;
    }

    @GetMapping("/world")
    public String world() {
        log.info("Calling World");
        return "World";
    }

    @GetMapping("/parse")
    public Flux<TrackPoint> parse() throws FileNotFoundException {
        InputStream inputStream = new BufferedInputStream(new FileInputStream("gpx.xml"));
        return xmlParser.parse(inputStream, TrackPoint.class, "tp");
    }

    @Data
    private static class TrackPoint {
        private Double lat;
        private Double lon;
    }

}
