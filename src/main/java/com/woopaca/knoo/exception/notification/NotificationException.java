package com.woopaca.knoo.exception.notification;

import com.woopaca.knoo.exception.KnooException;

public class NotificationException extends KnooException {

    public NotificationException(NotificationError notificationError) {
        super(notificationError);
    }
}
