package com.woopaca.knoo.service.impl;

import com.woopaca.knoo.controller.dto.auth.SignInUser;
import com.woopaca.knoo.controller.dto.notification.NotificationListResponseDto;
import com.woopaca.knoo.entity.Notification;
import com.woopaca.knoo.entity.Post;
import com.woopaca.knoo.entity.User;
import com.woopaca.knoo.entity.value.NotificationType;
import com.woopaca.knoo.exception.notification.NotificationNotFoundException;
import com.woopaca.knoo.repository.NotificationRepository;
import com.woopaca.knoo.service.AuthService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.springframework.data.domain.Sort.Direction.DESC;

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
        PageRequest pageRequest = PageRequest.of(page, DEFAULT_PAGE_SIZE, Sort.by(DESC, "notificationDate"));

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

    @Transactional
    public void generateNotification(final Long generatorId, final Post post, final NotificationType notificationType) {
        if (generatorId.equals(post.getWriter().getId())) {
            return;
        }

        Notification notification = Notification.of(generatorId, post, notificationType);
        notificationRepository.save(notification);
    }

    @Transactional
    public void removeNotification(final Long generatorId, final Post post, final NotificationType notificationType) {
        Optional<Notification> notificationOptional =
                notificationRepository.findAlreadyGeneratedNotification(generatorId, post, notificationType);
        notificationOptional.ifPresent(notificationRepository::delete);
    }
}
