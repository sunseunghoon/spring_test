package com.example.myapp;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;

@SpringBootApplication
@RestController
@Slf4j
@EnableAsync  // 까먹기 쉬운데 꼭 붙여줘야 Nonblocking-Async 모드가 제대로 동작함
public class TobyTvSpringWebfluxApplication {

    static final String URL1 = "8081/service1?req={req}";
    static final String URL2 = "8081/service2?req={req}";

    public static void main(String[] args) {
        SpringApplication.run(TobyTvSpringWebfluxApplication.class, args);
    }

    @Service
    public static class MyService {

        @Async
        public CompletableFuture<String> work(String req) {
            return CompletableFuture.completedFuture(req + "/asyncWork");
        }
    }

    @Autowired
    private MyService myService;

    // 기존의 AsyncRestTemplate 역할을 하는 WebClient
    // ThreadSafe 하다.
    WebClient webClient = WebClient.create();

    // 전체적으로 AsyncRestTemplate + CompletableFuture + DeferredResult과 같은 NonBlocking-Async 방식
    @GetMapping("/rest/{id}")
    public Mono<String> rest(@PathVariable("id") int idx) {
        // Mono<T>는 T에 여러 가지 기능을 추가해서 덮어씌운 Container 또는 Wrapper
        // Optional<T>도 T에 NullPointer 방지를 위한 여러 기능을 추가한다는 의미에서는 비슷

        // ClientResponse는 ResponseEntity와 유사
        Mono<ClientResponse> resMono =
                webClient.get()
                         .uri(URL1, idx)
                         .exchange();
        // 여기까지만으로는 파이프라인만 구성되며 실제 호출은 하지 않음(Stream과 유사)
        // publisher는 subscriber가 subscribe를 해야만 파이프라인을 실행해서 publish 한다.
        // return 타입이 Mono이면 Spring이 subscribe를 해서 파이프라인이 실행된다.

        // ClientResponse의 Body만 String으로 빼오기
        Mono<String> resultMono = resMono
                .flatMap(  // bodyToMono가 Mono<T>를 반환하므로 flatMap을 써야함
                        clientResponse -> clientResponse.bodyToMono(String.class))  // 결과 Mono<String>
                .doOnNext(c -> log.info(c))  // log로 Thread 이름 확인 - A
                // 아래와 같이 flatMap()을 통해 chaining하면 nonblocking-async를 순차적으로 처리 가능
                .flatMap(res1 -> webClient.get().uri(URL2, res1).exchange())  // 결과 Mono<ClientResponse>
                .flatMap(c -> c.bodyToMono(String.class))  // 결과 Mono<String>
                .doOnNext(c -> log.info(c))  // log로 Thread 이름 확인 - A
                .flatMap(res2 -> Mono.fromCompletionStage(myService.work(res2)))  // 결과 Mono<String>
                .doOnNext(c -> log.info(c));  // myService.work()를 실행하는 Thread 이름 확인 - B

        return resultMono;
    }
}