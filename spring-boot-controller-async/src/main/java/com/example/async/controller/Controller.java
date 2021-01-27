package com.example.async.controller;

import com.example.async.ruuner.AsyncFunction;
import com.example.async.service.CoreClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
public class Controller {

    @Autowired
    CoreClient coreClient;

    @Autowired
    AsyncFunction asyncFunction;

    @PostMapping(value = "/post")
    public void post(@RequestParam("code") int code,
                     @RequestParam("reason") String reason){

        log.debug("test code = {}, reason = {}", code, reason);

        //서비스 호출
        coreClient.serviceRequest(code, reason);

        return;
    }


    //////////////////////////////////////////////기존///////////////////////////////////
    @GetMapping(value = "/delay/{path}")
    public String getDelay(@PathVariable("path") Integer delay) throws InterruptedException {

        Thread.sleep(delay);
        return "done";
    }


    @PostMapping(value = "/test")
    public ResponseEntity<?> postDelay(@RequestParam(value = "sessionID") String sessionId,
                                       @RequestParam(value = "spid") String spid,
                                       @RequestParam(value = "parameter") String parameter) throws InterruptedException {

        log.info("sessionid = {}, spid = {}, parameter = {}", sessionId, spid, parameter);
        return ResponseEntity.status(200).build();
    }
}
