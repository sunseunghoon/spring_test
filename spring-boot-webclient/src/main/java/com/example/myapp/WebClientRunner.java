package com.example.myapp;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;


@Component
@Slf4j
@RequiredArgsConstructor
public class WebClientRunner implements ApplicationRunner {
//    private static final String URL = "http://192.168.7.158:18080/cid";

    private final WebClient.Builder webClientBuilder;


    @Override
    public void run(ApplicationArguments args) {
        long startTme = System.currentTimeMillis();

//        Mono<ResponseEntity> result = webClient.mutate()
//        webClient.mutate()
//                 .build()
//                 .post()
//                 .uri(URL)
//                 .body(BodyInserters.FormInserter())
//                 .body(WebClientConfig.formInserter()
//                                      .with("sessionID", "S0101-1")
//                                      .with("spid", "SPID")
//                                      .with("parameter", "ABCDEFGHIGHT")
//                 )
//                 .retrieve()
//                 .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
//                         (clientResponse) -> {
//                             return clientResponse.bodyToMono(String.class).map(RuntimeException::new);
//                         })
//                 .bodyToMono(ResponseEntity.class)
//                 .subscribe(ret -> {
//                     log.info("sunsh time2 [{}], {} {}", System.currentTimeMillis() - startTme,
//                             ret.getStatusCode(),
//                             ret.getStatusCodeValue());
//                 });

//        webClient.post()
//                 .uri(URL)
//                 .body(WebClientConfig.formInserter()
//                                      .with("sessionID", "S0101-1")
//                                      .with("spid", "SPID")
//                                      .with("parameter", "ABCDEFGHIGHT")
//                 )
//                 .retrieve()
//                 .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
//                         (clientResponse) -> {
//                             return clientResponse.bodyToMono(String.class).map(RuntimeException::new);
//                         })
//                 .bodyToMono(ResponseEntity.class)
//                 .subscribe(ret -> {
//                     log.info("sunsh time2 [{}], {} {}", System.currentTimeMillis() - startTme,
//                             ret.getStatusCode(),
//                             ret.getStatusCodeValue());
//                 });
        WebClient webClient = webClientBuilder.build();
        Mono<ClientResponse> result = webClient.post()
//                                                .uri("http://localhost:8080/test")
                                                .uri("http://192.168.7.158:18080/cid")
                                                .body(WebClientConfig.formInserter()
                                                                     .with("sessionID", "Cid_07070064957_007_AS_1611286225960")
                                                                     .with("spid", "OPN0001")
                                                                     .with("parameter", "WwMGL2J2W8yGxWxGqLcgCGjLLL90vrcstlFC105vhIgFshVY8E0pd26LNhICbOwpxuMt1oiX1vuvVjneKqEnpi1S9XMeqfXPplvr2%2FVN1cfFdB%2Fwe%2BnAcBL8GnBDYtfuX%2FxbhYWx2nVgcXPe4ritpqNeu7eqhRkB2Ahpbn0NK%2BE1jnWhtyOTMB9vRmBK4t1RmYiawkmvXzXvHB0zyW21Jg%3D%3D")
                                                )
                                                .exchange();
//                                                .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
//                                                        (clientResponse) -> {
//                                                            return clientResponse.bodyToMono(String.class).map(RuntimeException::new);
//                                                        })
//                                                .onStatus(status -> status.is2xxSuccessful(),
//                                                        (clientResponse) -> {
//                                                            return clientResponse.bodyToMono(ResponseEntity.class).flatMap(error -> {
//                                                                return Mono.error(new RuntimeException(error.getStatusCodeValue(), error.getStatusCode()));
//
//                                                            });
//                                                        })
//                                                .bodyToMono(ResponseEntity.class);

        log.info("sunsh1");

        result.subscribe((response) -> {

            // here you can access headers and status code
            ClientResponse.Headers headers = response.headers();
            HttpStatus stausCode = response.statusCode();
            log.info("{}, {}", response.headers(), response.statusCode());
            Mono<String> bodyToMono = response.bodyToMono(String.class);
            // the second subscribe to access the body
            bodyToMono.subscribe((body) -> {

                // here you can access the body
                log.info("body:" + body);

                // and you can also access headers and status code if you need
                log.info("headers:" + headers.asHttpHeaders());
                log.info("stausCode:" + stausCode);

            }, (ex) -> {
                // handle error
            });

        }, (ex) -> {
            // handle network error
        });
    }

}