package com.example.async.service;

import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;

@Component
public class CoreClient {

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
    public void serviceRequest(int code, String reason) {

        ListenableFuture<Result> future = null;  // grpcFutrueStub 호출한 결과를 받음.
//        future =


    }


    private ListenableFuture<Result> getResult(int code, String reason){

        return null;
//        return new Result.ResultBuilder()
//                .code(code)
//                .reason(reason)
//                .build();
    }
}
