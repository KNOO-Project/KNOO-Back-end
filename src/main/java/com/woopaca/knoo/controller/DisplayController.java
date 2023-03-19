package com.woopaca.knoo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class DisplayController {

    @GetMapping("/mail-verify")
    @ResponseBody
    public String mailVerifyComplete() {
        return "이메일 인증 완료";
    }
}
