package com.woopaca.knoo.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MailServiceTest {

    @Autowired
    MailService mailService;

    @Test
    @DisplayName("메일 전송")
    void mailSendTest() throws Exception {
        String receivedUserMail = "jcw001031@gmail.com";
        mailService.sendAuthMail(receivedUserMail);
    }
}