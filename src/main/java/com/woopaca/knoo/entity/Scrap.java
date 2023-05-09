package com.woopaca.knoo.entity;

import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "scrap")
public class Scrap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "scrap_id")
    private Long id;

    @Column(name = "scrap_date", nullable = false)
    private LocalDateTime scrapDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    protected Scrap() {
        scrapDate = LocalDateTime.now();
    }

    public static Scrap userScrapPost(final Post post, final User user) {
        Scrap scrap = new Scrap();
        scrap.post = post;
        scrap.user = user;
        post.getScraps().add(scrap);
        user.getScraps().add(scrap);
        return scrap;
    }
}
