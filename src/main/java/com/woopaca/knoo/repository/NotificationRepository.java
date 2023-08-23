package com.woopaca.knoo.repository;

import com.woopaca.knoo.entity.Notification;
import com.woopaca.knoo.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    boolean existsByIsReadFalseAndUser(User user);

    @EntityGraph(value = "Notification.post")
    Page<Notification> findByUser(User user, Pageable pageable);
}
