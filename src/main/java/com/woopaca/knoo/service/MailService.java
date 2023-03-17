package com.woopaca.knoo.service;

public interface MailService {

    void sendAuthMail(final String receivedUserMail, final String uuid);
}
