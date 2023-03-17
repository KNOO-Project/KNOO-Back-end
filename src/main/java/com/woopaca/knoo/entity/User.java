package com.woopaca.knoo.entity;

import com.woopaca.knoo.controller.dto.SignUpRequestDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCrypt;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;
    @Column(unique = true)
    private String username;
    private String password;
    @Column(unique = true)
    private String name;
    @Column(unique = true)
    private String email;
    private String joinDate;
    @Enumerated(value = EnumType.STRING)
    private EmailVerify emailVerify;
    private String campus;
    private String major;

    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private Verification verification;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "Role", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role_name")
    private List<String> roles = new ArrayList<>();

    @Builder
    public User(String username, String password, String name, String email,
                String joinDate, EmailVerify emailVerify, String campus, String major) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.email = email;
        this.joinDate = joinDate;
        this.emailVerify = emailVerify;
        this.campus = campus;
        this.major = major;
    }

    public void setVerification(Verification verification) {
        this.verification = verification;
        verification.setUser(this);
    }

    public void verify() {
        emailVerify = EmailVerify.ENABLE;
    }

    public static User join(final SignUpRequestDto signUpRequestDto) {
        User user = User.builder()
                .username(signUpRequestDto.getUsername())
                .password(BCrypt.hashpw(signUpRequestDto.getPassword(),
                        BCrypt.gensalt()))
                .name(signUpRequestDto.getName())
                .email(signUpRequestDto.getEmail())
                .joinDate(LocalDateTime.now().toString())
                .emailVerify(EmailVerify.DISABLE)
                .build();
        user.roles.add("ROLE_USER");
        return user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream().map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
