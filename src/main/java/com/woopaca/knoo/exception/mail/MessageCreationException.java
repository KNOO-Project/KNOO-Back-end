package com.woopaca.knoo.exception.mail;

import com.woopaca.knoo.exception.KnooException;

public class MessageCreationException extends KnooException {

    public MessageCreationException() {
        super(MailError.MESSAGE_CAN_NOT_CREATE);
    }
}
