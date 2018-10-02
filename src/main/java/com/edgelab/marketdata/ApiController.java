package com.edgelab.marketdata;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ApiController {

    private final RestTemplate rest;

    @GetMapping("/hello")
    public Mono<String> hello() {
        log.info("Calling Hello");

        Mono<String> response = Mono.fromCallable(() -> rest.getForObject("http://localhost:8080/world", String.class))
            .log()
            .subscribeOn(Schedulers.elastic());

        return response.map(resp -> "Hello " + resp);
    }

    @GetMapping("/world")
    public Mono<String> world() {
        log.info("Calling World");

        return Mono.just("World");
    }

}
