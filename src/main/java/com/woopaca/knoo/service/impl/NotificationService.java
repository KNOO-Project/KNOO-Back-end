package com.woopaca.knoo.service.impl;

import com.woopaca.knoo.controller.dto.auth.SignInUser;
import com.woopaca.knoo.entity.User;
import com.woopaca.knoo.repository.NotificationRepository;
import com.woopaca.knoo.service.AuthService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final AuthService authService;

    public NotificationService(NotificationRepository notificationRepository, AuthService authService) {
        this.notificationRepository = notificationRepository;
        this.authService = authService;
    }

    public boolean hasUnreadNotifications(final SignInUser signInUser) {
        User authenticatedUser = authService.getAuthenticatedUser(signInUser);
        return notificationRepository.existsByIsReadFalseAndUser(authenticatedUser);
    }
}
