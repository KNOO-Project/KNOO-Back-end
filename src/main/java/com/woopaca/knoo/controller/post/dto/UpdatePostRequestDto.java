package com.woopaca.knoo.controller.post.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class UpdatePostRequestDto {

    @JsonAlias(value = "post_title")
    @NotBlank(message = "게시글 제목은 비어있을 수 없습니다.")
    @Size(min = 2, max = 50, message = "게시글 제목은 2자 이상, 50자 이하이어야 합니다.")
    private String postTitle;

    @JsonAlias(value = "post_content")
    @NotBlank(message = "게시글 본문은 비어있을 수 없습니다.")
    @Size(min = 2, max = 50, message = "게시글 본문은 2자 이상, 4000자 이하이어야 합니다.")
    private String postContent;

    @JsonAlias(value = "anonymous")
    @NotNull(message = "익명 선택 정보가 비어있습니다.")
    private Boolean isAnonymous;
}
