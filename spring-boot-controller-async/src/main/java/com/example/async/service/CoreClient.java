package com.example.async.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.google.common.util.concurrent.ListenableFuture;

import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Component
public class CoreClient {

    private static ExecutorService e = Executors./*newCachedThreadPool*/newFixedThreadPool(30, new BasicThreadFactory.Builder()
            .namingPattern("Asyn-Listener-%d")
            .daemon(true)
            .priority(Thread.MAX_PRIORITY)
            .build());

    @Autowired
    CallClient callClient;

    @Autowired
    CoreListener coreListener;

//        ListenableFuture의 구현체로는 AsyncResult, CompletableToListenableFutureAdapter,
//              ListenableFutureAdapter, ListenableFutureTask, SettableListenableFuture 등이 있다.
//
//        void addCallback(ListenableFutureCallback<? super T> callback) : ListenableFutureCallback를 등록한다.
//
//        void addCallback(SuccessCallback<? super T> successCallback, FailureCallback failureCallback) :
//              자바 8의 람다를 사용할 수 있도록 성공 콜백과 실패 콜백을 각각 등록한다.
//              completable() :ListenableFuture 객체를 CompletableFuture로 변환해서 리턴한다.
//          SettableListenableFuture는 ListenableFuture의 구현체로 set 메소드 또는 setException 메소드로 결과를 등록할 수 있다.
//
//        public boolean set(@Nullable T value) : Future의 결과를 등록한다.
//                  만약에 결과가 성공적으로 등록되었다면 true를 리턴한다. 만약 결과가 이미 등록되었거나 cancell된 경우에는 false를 리턴한다.

//        public boolean setException(Throwable exception) : Future에 예외를 등록한다.
//                  만약 예외가 성공적으로 등록되었다면 true를 리턴한다. 만약에 Future의 결과가 이미 등록되었거나 cancell된 경우에는 false를 리턴한다.
    /**
     * 메소드 실행 시 ListenerbleFuture addListener 한다.
     */
    public void clickRequest(int code, String reason) throws InterruptedException {

        log.info("click 서비스 요청이 들어옴. 비동기로 서비스 요청 처리를 시작한다.");
        ListenableFuture<Result> f = callClient.clickServiceReq(code, reason);  // grpcFutrueStub 호출한 결과를 받음.
        
        //addListener(Runnable r, Executor e)
        //비동기 결과를 처리할 부분을 listener로 등록
        f.addListener( coreListener.clickListener(f), e);
        log.info("click 서비스 요청 메소드는 종료. 실제 처리는 비동기로 처리");
    }

    public void dragRequest(int code, String reason, HttpServletResponse httpServletResponse) throws InterruptedException {

        log.info("drag 서비스 요청이 들어옴. 비동기로 서비스 요청 처리를 시작한다.");
        ListenableFuture<Result> f = callClient.dragServiceReq(code, reason);  // grpcFutrueStub 호출한 결과를 받음.

        //addListener(Runnable r, Executor e)
        //비동기 결과를 처리할 부분을 listener로 등록
        f.addListener( coreListener.dragListener(f, httpServletResponse), e);
        log.info("drag 서비스 요청 메소드는 종료. 실제 처리는 비동기로 처리");
    }
}
