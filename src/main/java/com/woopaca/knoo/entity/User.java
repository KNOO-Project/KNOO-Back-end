package com.woopaca.knoo.entity;

import com.woopaca.knoo.controller.dto.auth.SignUpRequestDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "join_date", nullable = false)
    private LocalDateTime joinDate;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "email_verify", nullable = false)
    private EmailVerify emailVerify;

    @Column(name = "verification_code", nullable = false)
    private String verificationCode;

    @Column(name = "campus")
    private String campus;

    @Column(name = "major")
    private String major;

    @OneToMany(mappedBy = "writer")
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<PostLike> postLikes = new ArrayList<>();

    @OneToMany(mappedBy = "writer")
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<CommentLike> commentLikes = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "Role", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role_name")
    private List<String> roles = new ArrayList<>();

    @Builder
    public User(String username, String password, String name, String email,
                LocalDateTime joinDate, EmailVerify emailVerify, String verificationCode, String campus, String major) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.email = email;
        this.joinDate = joinDate;
        this.emailVerify = emailVerify;
        this.verificationCode = verificationCode;
        this.campus = campus;
        this.major = major;
    }

    public void verify() {
        emailVerify = EmailVerify.ENABLE;
    }

    public static User join(final SignUpRequestDto signUpRequestDto) {
        return createDefaultUser(signUpRequestDto);
    }

    private static User createDefaultUser(SignUpRequestDto signUpRequestDto) {
        User user = User.builder()
                .username(signUpRequestDto.getUsername())
                .password((signUpRequestDto.getPassword()))
                .name(signUpRequestDto.getName())
                .email(signUpRequestDto.getEmail())
                .joinDate(LocalDateTime.now())
                .emailVerify(EmailVerify.DISABLE)
                .verificationCode(UUID.randomUUID().toString())
                .build();
        user.roles = Collections.singletonList("ROLE_USER");
        return user;
    }

    public void encodePassword(final PasswordEncoder passwordEncoder) {
        password = passwordEncoder.encode(password);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream().map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
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
