package com.woopaca.knoo.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Verification {

    @Id
    @OneToOne(fetch = FetchType.LAZY)
    @Column(name = "user_id")
    private User user;
    @Column(unique = true)
    private String verification_code;

    @Builder
    public Verification(User user, String verification_code) {
        this.user = user;
        this.verification_code = verification_code;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
