package com.woopaca.knoo.entity.value;

import lombok.Getter;

@Getter
public enum NotificationType {

    COMMENT("회원님의 게시글에 새로운 댓글이 달렸습니다."),
    LIKE("다른 회원이 회원님의 게시글에 좋아요를 눌렀습니다."),
    REPLY("회원님의 댓글에 새로운 답변이 달렸습니다."),
    SCRAP("다른 회원이 회원님의 게시글을 스크랩 했습니다.");

    private final String description;

    NotificationType(String description) {
        this.description = description;
    }
}
