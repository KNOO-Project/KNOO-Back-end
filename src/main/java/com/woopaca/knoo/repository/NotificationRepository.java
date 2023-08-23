package com.woopaca.knoo.repository;

import com.woopaca.knoo.entity.Notification;
import com.woopaca.knoo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    boolean existsByIsReadFalseAndUser(User user);
}
