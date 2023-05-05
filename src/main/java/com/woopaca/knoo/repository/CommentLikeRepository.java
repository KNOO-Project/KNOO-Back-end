package com.woopaca.knoo.repository;

import com.woopaca.knoo.entity.Comment;
import com.woopaca.knoo.entity.CommentLike;
import com.woopaca.knoo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {

    Optional<CommentLike> findByCommentAndUser(Comment comment, User user);
}
