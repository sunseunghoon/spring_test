package com.example.async.ruuner;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;

@Slf4j
@Component
public class AsyncFunction {

    /**
     * @Async는 다른 클래스, public, return은 void나 Future<v>만 가능
     */
    @Async
    public void logger() throws InterruptedException {
        Thread.sleep(1000);
        log.info("서비스1");  // async 스레드에서 출력
    }


    @Async
    public ListenableFuture<String> logger2() throws InterruptedException {
        Thread.sleep(1000);
        log.info("서비스2");  // async 스레드에서 출력
        return new AsyncResult<>("결과2"); //async 결과 리턴
    }

    @Async
    public ListenableFuture<String> logger3() throws InterruptedException {
        Thread.sleep(1000);
        log.info("서비스3");  // async 스레드에서 출력
//        throw new RuntimeException();
        // {ThreadPoolTaskExecutor-3} null - (AsyncRunner3.java:50)
        // java.lang.RuntimeException: null

        return new AsyncResult<>("결과3"); //async 결과 리턴
    }
}

