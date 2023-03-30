package com.woopaca.knoo.controller.comment;

import com.woopaca.knoo.controller.comment.dto.WriteCommentRequestDto;
import com.woopaca.knoo.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RestController
@RequestMapping("/api/v1/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/{postId}")
    public ResponseEntity<String> writeNewComment(
            @RequestHeader(AUTHORIZATION) final String authorization,
            @RequestBody @Valid final WriteCommentRequestDto writeCommentRequestDto,
            @PathVariable("postId") final Long postId
    ) {
        commentService.writeComment(writeCommentRequestDto, postId, authorization);
        return ResponseEntity.created(URI.create("")).body("댓글 작성이 완료되었습니다.");
    }
}
