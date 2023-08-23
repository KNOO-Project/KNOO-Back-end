package com.woopaca.knoo.exception.notification;

public class NotificationNotFoundException extends NotificationException {

    public NotificationNotFoundException() {
        super(NotificationError.NOTIFICATION_NOT_FOUND);
    }
}
