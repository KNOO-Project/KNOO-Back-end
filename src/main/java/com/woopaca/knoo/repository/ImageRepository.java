package com.woopaca.knoo.repository;

import com.woopaca.knoo.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
