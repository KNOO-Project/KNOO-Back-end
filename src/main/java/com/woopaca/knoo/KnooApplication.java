package com.woopaca.knoo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class KnooApplication {

    public static void main(String[] args) {
        SpringApplication.run(KnooApplication.class, args);
    }

}
