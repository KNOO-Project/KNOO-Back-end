package com.woopaca.knoo.controller;

import com.woopaca.knoo.annotation.SignIn;
import com.woopaca.knoo.controller.dto.PageDto;
import com.woopaca.knoo.controller.dto.auth.SignInUser;
import com.woopaca.knoo.controller.dto.notification.NotificationListResponseDto;
import com.woopaca.knoo.service.impl.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RequestMapping("/api/notifications")
@RestController
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping("/unread")
    public ResponseEntity<Boolean> checkUnreadNotifications(@SignIn final SignInUser signInUser) {
        boolean hasUnreadNotifications = notificationService.hasUnreadNotifications(signInUser);
        return ResponseEntity.ok().body(hasUnreadNotifications);
    }

    @GetMapping("")
    public ResponseEntity<NotificationListResponseDto> userNotifications(
            @SignIn final SignInUser signInUser, @ModelAttribute @Valid final PageDto pageDto
    ) {
        NotificationListResponseDto notificationListResponseDto =
                notificationService.getUserNotifications(signInUser, pageDto.getPage() - 1);
        return ResponseEntity.ok().body(notificationListResponseDto);
    }

    @PutMapping("/{notification_id}")
    public ResponseEntity<String> readNotification(
            @SignIn final SignInUser signInUser, @PathVariable final Long notification_id
    ) {
        notificationService.readNotification(notification_id, signInUser);
        return ResponseEntity.ok().body(String.format("User read notification [%s]", notification_id));
    }

    @PutMapping("")
    public ResponseEntity<String> markAllAsRead(@SignIn final SignInUser signInUser) {
        notificationService.markAllNotificationsAsRead(signInUser);
        return ResponseEntity.ok().body("User read all notifications.");
    }
}
