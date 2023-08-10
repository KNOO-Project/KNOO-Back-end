package com.woopaca.knoo.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@RequestMapping("/api/notifications")
@RestController
public class NotificationController {

    private SseEmitter sseEmitter;

    @GetMapping(value = "/sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter createConnection() {
        sseEmitter = new SseEmitter();
        log.info("SSE connection");
        try {
            sseEmitter.send("Hello!");
        } catch (IOException e) {
            sseEmitter.completeWithError(e);
        }

        ExecutorService executor = Executors.newSingleThreadExecutor();

        //Creating a new thread for async processing
        executor.execute(() -> {
        });
        executor.shutdown();

        sseEmitter.onCompletion(() -> log.info("Sse Complete"));
        return sseEmitter;
    }

    @PostMapping("/sse")
    public void inputData(@RequestParam(name = "data") String data) {
        try {
            sseEmitter.send(SseEmitter.event().data(data));
            log.info("data = {}", data);
        } catch (IOException e) {
            sseEmitter.completeWithError(e);
        }
    }
}
