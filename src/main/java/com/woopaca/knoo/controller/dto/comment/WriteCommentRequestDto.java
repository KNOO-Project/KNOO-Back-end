package com.woopaca.knoo.controller.dto.comment;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class WriteCommentRequestDto {

    @JsonAlias(value = "comment_content")
    @NotBlank(message = "댓글 내용은 비어있을 수 없습니다.")
    private String commentContent;
}
