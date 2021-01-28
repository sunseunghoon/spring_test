package com.example.async.controller;

import com.example.async.ruuner.AsyncFunction;
import com.example.async.service.CoreClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;


@Slf4j
@RestController
public class Controller {

    @Autowired
    CoreClient coreClient;

    @Autowired
    AsyncFunction asyncFunction;

    /**
     * http://localhost:8080/click?code=102&reason=success
     *
     * 무조건 성공 후 비동기 처리
     */
    // http://localhost:8080/click?code=102&reason=success
    @GetMapping(value = "/click")
    public ResponseEntity<?> click(@RequestParam("code") int code,
                     @RequestParam("reason") String reason) throws InterruptedException {

        log.info("최초 click 컨트롤러 요청 들어옴. code = {}, reason = {}", code, reason);

        //서비스 호출
        coreClient.clickRequest(code, reason);

        log.info("click 컨트롤러 종료");
        return ResponseEntity.status(200).body("요청 성공");
    }


    /**
     * http://localhost:8080/drag?code=102&reason=success
     *
     * 비동기 리턴을 처리하려고 했지만 구현 불가.. webflux 필요
     */
    @GetMapping(value = "/drag")
    public void drag(@RequestParam("code") int code,
                     @RequestParam("reason") String reason,
                     HttpServletResponse httpServletResponse) throws InterruptedException {

        log.info("최초 drag 컨트롤러 요청 들어옴. code = {}, reason = {}", code, reason);

        //서비스 호출
        coreClient.dragRequest(code, reason, httpServletResponse);

        log.info("drag 컨트롤러 종료");
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
