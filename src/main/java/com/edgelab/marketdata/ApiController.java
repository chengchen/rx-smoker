package com.edgelab.marketdata;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ApiController {

    private final RestTemplate rest;

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

}
