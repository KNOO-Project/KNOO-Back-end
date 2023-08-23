package com.woopaca.knoo.repository;

import com.woopaca.knoo.entity.Notification;
import com.woopaca.knoo.entity.Post;
import com.woopaca.knoo.entity.User;
import com.woopaca.knoo.entity.value.NotificationType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    boolean existsByIsReadFalseAndUser(User user);

    @EntityGraph(value = "Notification.post")
    Page<Notification> findByUser(User user, Pageable pageable);

    @Query(value = "SELECT n FROM Notification n " +
            "WHERE n.generatorId = :generatorId AND n.post = :post AND n.notificationType = :type")
    Optional<Notification> findAlreadyGeneratedNotification(
            @Param(value = "generatorId") Long generatorId,
            @Param(value = "post") Post post,
            @Param(value = "type") NotificationType notificationType
    );
}
