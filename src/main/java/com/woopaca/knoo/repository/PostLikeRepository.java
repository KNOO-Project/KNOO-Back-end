package com.woopaca.knoo.repository;

import com.woopaca.knoo.entity.Post;
import com.woopaca.knoo.entity.PostLike;
import com.woopaca.knoo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {

    Optional<PostLike> findByPostAndUser(Post post, User user);
}
