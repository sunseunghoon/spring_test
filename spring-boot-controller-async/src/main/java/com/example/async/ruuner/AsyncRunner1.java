package com.example.async.ruuner;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AsyncRunner1 implements ApplicationRunner {

    @Autowired
    AsyncFunction asyncFunction;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("컨트롤러1 시작"); //main스레드에서 실행

        asyncFunction.logger(); //@async함수 호출. 함수는 async스레드에서 처리함

        log.info("컨트롤러1 끝"); //main스레드에서 실행
    }
}
