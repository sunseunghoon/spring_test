package com.example.myapp;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.ResolvableType;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ClientHttpRequest;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.http.codec.FormHttpMessageWriter;
import org.springframework.http.codec.LoggingCodecSupport;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.nio.charset.Charset;

@Slf4j
@Configuration
public class WebClientConfig {

    @Bean
    public WebClient webClient() {
        ExchangeStrategies exchangeStrategies = ExchangeStrategies.builder()
                .codecs(configurator ->
                        configurator.defaultCodecs()
                                //default 256kb to 50kb
                                .maxInMemorySize(1024 * 1024 * 50))
                .build();
        //logging
        exchangeStrategies
                .messageWriters().stream()

                .filter(LoggingCodecSupport.class::isInstance)
                .forEach(writer -> ((LoggingCodecSupport) writer).setEnableLoggingRequestDetails(true));

//        MediaType mediaType = new MediaType("application", "x-www-form-urlencoded; charset=EUC-KR");
        MediaType mediaType = new MediaType("application", "x-www-form-urlencoded", Charset.forName("EUC-KR"));

        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector())
                .exchangeStrategies(exchangeStrategies)
                .filter(ExchangeFilterFunction.ofRequestProcessor(
                        //request header 출력
                        clientRequest -> {
                            log.debug("Request: {} {}", clientRequest.method(), clientRequest.url());
                            clientRequest.headers().forEach((name, values) -> values.forEach(value -> log.debug("Request Headers: {} : {}", name, value)));
                            return Mono.just(clientRequest);
                        }
                ))
                .filter(ExchangeFilterFunction.ofResponseProcessor(
                        //response header 출력
                        clientResponse -> {
                            log.debug("Response: statusCode:{}", clientResponse.statusCode());
                            clientResponse.headers().asHttpHeaders().forEach((name, values) -> values.forEach(
                                    value -> log.debug("Response Header: {} : {}", name, value)));
                            return Mono.just(clientResponse);
                        }
                ))
                //default header
                .defaultHeader(HttpHeaders.CONNECTION, "close")
//                        .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED)
//                        .defaultHeader(HttpHeaders.CONTENT_TYPE, mediaType)
//                        .defaultHeader(HttpHeaders.CONTENT_ENCODING,"EUC-KR")
//                        .defaultHeader("user-agent", "Chrome/78.0.3904.87 Safari/537.3")
                .build();
    }

    /**
     * This method is unfortunately necessary because of Spring Webflux's propensity to add {@code ";charset=..."}
     * to the {@code Content-Type} header, which the Generic Chinese Device doesn't handle properly.
     *
     * @return a {@link BodyInserters.FormInserter} that doesn't add the character set to the content type header
     */
    public static BodyInserters.FormInserter<String> formInserter() {

        return new BodyInserters.FormInserter<String>() {

            private final MultiValueMap<String, String> data = new LinkedMultiValueMap<>();

            @Override
            public BodyInserters.FormInserter<String> with(final String key, final String value) {
                data.add(key, value);
                return this;
            }

            @Override
            public BodyInserters.FormInserter<String> with(final MultiValueMap<String, String> values) {
                data.addAll(values);
                return this;
            }

            @Override
            public Mono<Void> insert(final ClientHttpRequest outputMessage, final Context context) {
                final ResolvableType formDataType =
                        ResolvableType.forClassWithGenerics(MultiValueMap.class, String.class, String.class);
                return new FormHttpMessageWriter() {
                    @Override
                    protected MediaType getMediaType(final MediaType mediaType) {
                        if (MediaType.APPLICATION_FORM_URLENCODED.equals(mediaType)) {
                            return new MediaType("application", "x-www-form-urlencoded", Charset.forName("EUC-KR"));

                        } else {
                            return super.getMediaType(mediaType);
                        }
                    }
                }.write(Mono.just(this.data), formDataType,
                        MediaType.APPLICATION_FORM_URLENCODED,
                        outputMessage,
                        context.hints());
            }
        };
    }
}

