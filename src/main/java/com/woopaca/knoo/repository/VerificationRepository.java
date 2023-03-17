package com.woopaca.knoo.repository;

import com.woopaca.knoo.entity.Verification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VerificationRepository extends JpaRepository<Verification, Long> {

    @Query("SELECT v FROM Verification v JOIN FETCH v.user WHERE v.verification_code = :code")
    Optional<Verification> findByCodeWithUser(@Param("code") final String code);
}
