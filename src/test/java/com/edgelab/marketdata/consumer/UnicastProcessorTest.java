package com.edgelab.marketdata.consumer;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.reactivestreams.Publisher;
import reactor.core.publisher.UnicastProcessor;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;

@RunWith(MockitoJUnitRunner.class)
public class UnicastProcessorTest {

    @Test
    public void testWithoutBuffer() {
        final UnicastProcessor<String> processor = UnicastProcessor.create();

        final Publisher<Integer> publisher = processor.map(String::length);

        processor.onNext("foo");
        processor.onNext("foobar");

        StepVerifier.create(publisher)
            .expectSubscription()
            .expectNext(3)
            .expectNext(6)
            .thenCancel()
            .log().verify();
    }

    @Test
    public void testWithBuffer() {
        final UnicastProcessor<String> processor = UnicastProcessor.create();

        final Publisher<Integer> publisher = processor
            .bufferTimeout(5, Duration.ofSeconds(1))
            .map(l -> l.stream().map(String::length).collect(toList()))
            .flatMapIterable(Function.identity());

        processor.onNext("foo");
        processor.onNext("foobar");

        StepVerifier.withVirtualTime(() -> publisher)
            .expectSubscription()
            .thenAwait(Duration.ofSeconds(2))
            .expectNext(3)
            .expectNext(6)
            .thenCancel()
            .log().verify();
    }

}
