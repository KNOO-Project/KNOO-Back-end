package com.woopaca.knoo.controller;

import com.woopaca.knoo.annotation.SignIn;
import com.woopaca.knoo.controller.dto.auth.SignInUser;
import com.woopaca.knoo.service.impl.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
