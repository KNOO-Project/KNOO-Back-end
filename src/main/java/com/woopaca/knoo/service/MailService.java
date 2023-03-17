package com.woopaca.knoo.service;

import javax.mail.MessagingException;

public interface MailService {

    String sendAuthMail(final String receivedUserMail, final String uuid)
            throws MessagingException;
}
