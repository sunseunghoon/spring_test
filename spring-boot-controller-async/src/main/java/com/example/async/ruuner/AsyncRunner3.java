package com.example.async.ruuner;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;

@Slf4j
@Component
public class AsyncRunner3 implements ApplicationRunner {

    @Autowired
    AsyncFunction asyncFunction;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        Thread.sleep(10000);
//        Future<String> future = asyncFunction.logger2(); //@async함수 호출. 함수는 async스레드에서 처리함
        log.info("컨트롤러3 시작"); //main스레드에서 실행
//        while(true){
//            if(future.isDone()){
//                log.info("컨트롤러2 async결과: " + future.get());  //future.get 가져온걸 찍음..
//                break;
//            }
//        } //블록킹

        /*
        ListenableFuture<String> f = asyncFunction.logger3();
        f.addCallback(result -> {
            //SuccessCallback
            log.info(result);
        }, //FailureCallback
                e -> {
                    log.error(e.getMessage(), e);
        });
         */

        ListenableFuture<String> f = asyncFunction.logger3();
        f.addCallback(result -> {
                    //SuccessCallback
                    log.info(result);
                }, //FailureCallback
                e -> {
                    log.error(e.getMessage(), e);
                });
        log.info("컨트롤러3 끝"); //main스레드에서 실행
    }
}
