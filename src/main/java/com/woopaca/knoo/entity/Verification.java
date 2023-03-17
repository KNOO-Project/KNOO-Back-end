package com.woopaca.knoo.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Verification {

    @Id
    private Long id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    @Column(unique = true)
    private String verification_code;

    private Verification(String verification_code) {
        this.verification_code = verification_code;
    }

    public static Verification createVerification() {
        String uuid = UUID.randomUUID().toString();
        return new Verification(uuid);
    }

    public void setUser(User user) {
        this.user = user;
    }
}
