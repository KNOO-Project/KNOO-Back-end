package com.woopaca.knoo.service;

import javax.mail.MessagingException;

public interface MailService {

    String sendAuthMail(final String receivedUserMail) throws MessagingException;
}
