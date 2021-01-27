package com.example.myapp;

import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.xml.ws.Response;

@Slf4j
@RestController
public class Controller {

    @GetMapping(value = "/delay/{path}")
    public String getDelay(@PathVariable("path") Integer delay) throws InterruptedException {

        Thread.sleep(delay);


        return "done";
    }
    @PostMapping(value = "/test")
    public ResponseEntity<?> postDelay(@RequestParam(value = "sessionID") String sessionId,
                                       @RequestParam(value = "spid") String spid,
                                       @RequestParam(value = "parameter") String parameter) throws InterruptedException {

        log.info("{}, {}, {}", sessionId, spid, parameter);
        return ResponseEntity.status(200).build();
    }
}
