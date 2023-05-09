package com.woopaca.knoo.repository;

import com.woopaca.knoo.entity.Post;
import com.woopaca.knoo.entity.Scrap;
import com.woopaca.knoo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ScrapRepository extends JpaRepository<Scrap, Long> {

    Optional<Scrap> findByPostAndUser(Post post, User user);
}
