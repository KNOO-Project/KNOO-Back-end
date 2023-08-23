package com.woopaca.knoo.controller.dto.notification;

import com.woopaca.knoo.entity.Notification;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class NotificationListResponseDto {

    private final int totalPages;
    private final List<NotificationListDto> notifications;

    public NotificationListResponseDto(int totalPages, List<NotificationListDto> notifications) {
        this.totalPages = totalPages;
        this.notifications = notifications;
    }

    public static NotificationListResponseDto of(final Page<Notification> notifications) {
        List<NotificationListDto> notificationList = notifications.stream()
                .map(NotificationListDto::of)
                .collect(Collectors.toList());
        return new NotificationListResponseDto(notifications.getTotalPages(), notificationList);
    }
}
