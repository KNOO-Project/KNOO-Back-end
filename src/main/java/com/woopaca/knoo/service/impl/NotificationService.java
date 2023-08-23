package com.woopaca.knoo.service.impl;

import com.woopaca.knoo.controller.dto.auth.SignInUser;
import com.woopaca.knoo.controller.dto.notification.NotificationListResponseDto;
import com.woopaca.knoo.entity.Notification;
import com.woopaca.knoo.entity.User;
import com.woopaca.knoo.exception.notification.NotificationNotFoundException;
import com.woopaca.knoo.repository.NotificationRepository;
import com.woopaca.knoo.service.AuthService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class NotificationService {

    private static final int DEFAULT_PAGE_SIZE = 20;

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

    public NotificationListResponseDto getUserNotifications(final SignInUser signInUser, final int page) {
        User authenticatedUser = authService.getAuthenticatedUser(signInUser);
        PageRequest pageRequest = PageRequest.of(page, DEFAULT_PAGE_SIZE);

        Page<Notification> notifications =
                notificationRepository.findByUser(authenticatedUser, pageRequest);
        return NotificationListResponseDto.of(notifications);
    }

    @Transactional
    public void readNotification(final Long notificationId, final SignInUser signInUser) {
        User authenticatedUser = authService.getAuthenticatedUser(signInUser);
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(NotificationNotFoundException::new);
        notification.readByUser(authenticatedUser);
    }
}
