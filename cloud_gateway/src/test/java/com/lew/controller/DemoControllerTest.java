package com.lew.controller;

import org.junit.Test;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author Yolen
 * @date 2022/2/4
 */
public class DemoControllerTest {

    private String baseUrl = "http://localhost:8093";


    @Test
    public void testMonoDefer() {
        Mono<String> monoJust = Mono.just(baseUrl);
        Mono<String> monoDefer = Mono.defer(() -> Mono.just(baseUrl));
        System.out.println("---  first subscribe  ---");
        monoJust.subscribe(System.out::println);
        monoDefer.subscribe(System.out::println);
        System.out.println("---  subscribe after baseUrl changed  ---");
        baseUrl = "https://localhost:8093";
        monoJust.subscribe(System.out::println);
        monoDefer.subscribe(System.out::println);
        System.out.println("-----------------------------------------");
        Mono<Long> clock = Mono.just(System.currentTimeMillis());
        //time == t0
        try { TimeUnit.SECONDS.sleep(1); } catch (InterruptedException e) {e.printStackTrace();}
        //time == t10
        System.out.println(clock.block());
        try { TimeUnit.SECONDS.sleep(2); } catch (InterruptedException e) {e.printStackTrace();}
        //time == t17
        System.out.println(clock.block()); //we re-subscribe to deferClock, still returns t0
        System.out.println("-----------------------------------------");
        Mono<Long> deferClock = Mono.defer(() -> Mono.just(System.currentTimeMillis()));
        //time == t0
        try { TimeUnit.SECONDS.sleep(1); } catch (InterruptedException e) {e.printStackTrace();}
        //time == t10
        System.out.println(deferClock.block());
        try { TimeUnit.SECONDS.sleep(2); } catch (InterruptedException e) {e.printStackTrace();}
        //time == t17
        System.out.println(deferClock.block()); //we re-subscribe to deferClock, still returns t0



    }

    @Test
    public void testReactor() {
        // 1.会直接触发 Hi there
//        Mono<String> result = Mono.just("Some payload").switchIfEmpty(asyncAlternative());
        // 2.不会直接触发 Hi there
//        Mono<String> result = Mono.just("Some payload")
//                .switchIfEmpty(Mono.defer(() -> asyncAlternative()));
        Mono<String> resultTriggered = Mono.<String>empty().switchIfEmpty(Mono.defer(() -> asyncAlternative()));
        // 订阅时才会触发 Hi there
        resultTriggered.subscribe(System.out::print);
    }

    private Mono<String> asyncAlternative() {
        return Mono.fromFuture(CompletableFuture.supplyAsync(() -> {
            System.out.println("Hi there");
            return "Alternative";
        }));
    }

    @Test
    public void testFlatMap() {
        System.out.println("######map#####");
        Flux.just("item1", "item2", "item3")
                .map(s -> s + ":" + System.currentTimeMillis())
                .subscribe(System.out::println);
        System.out.println("######flatMap#####");
        Flux.just("item1", "item2", "item3")
                .flatMap(s -> Flux.just(s + ":" + System.currentTimeMillis()))
                .subscribe(System.out::println);
    }

    @Test
    public void testMono() {
        String str = null;
        // NullPointerException
//        Mono.just(str).subscribe(System.out::println);
        System.out.println("---------------");
        Mono.justOrEmpty(str).subscribe(System.out::println);
    }

    @Test
    public void testWebClient() {
        WebClient webClient = WebClient.create(baseUrl);
    }

}