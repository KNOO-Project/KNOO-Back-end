package com.woopaca.knoo.controller.dto.notification;

import com.woopaca.knoo.common.DateFormatter;
import com.woopaca.knoo.entity.Notification;
import lombok.Builder;
import lombok.Getter;

@Getter
public class NotificationListDto {

    private final String notificationDescription;
    private final String notificationDate;
    private final String notificationType;
    private final boolean isRead;
    private final Long postId;

    @Builder
    protected NotificationListDto(String notificationDescription, String notificationDate, String notificationType, boolean isRead, Long postId) {
        this.notificationDescription = notificationDescription;
        this.notificationDate = notificationDate;
        this.notificationType = notificationType;
        this.isRead = isRead;
        this.postId = postId;
    }

    public static NotificationListDto of(final Notification notification) {
        return NotificationListDto.builder()
                .notificationDescription(notification.getNotificationDescription())
                .notificationDate(notification.getNotificationDate().format(DateFormatter.getFormatter()))
                .notificationType(notification.getNotificationType().toString())
                .isRead(notification.isRead())
                .postId(notification.getPost().getId())
                .build();
    }
}
