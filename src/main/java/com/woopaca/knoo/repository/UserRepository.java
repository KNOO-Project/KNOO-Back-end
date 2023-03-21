package com.woopaca.knoo.repository;

import com.woopaca.knoo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    Boolean existsByName(String name);

    Optional<User> findByVerificationCode(String verificationCode);
}
