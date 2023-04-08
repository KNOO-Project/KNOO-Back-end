package com.woopaca.knoo.controller.comment;

import com.woopaca.knoo.controller.comment.dto.WriteCommentRequestDto;
import com.woopaca.knoo.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RestController
@RequestMapping("/api/v1/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<String> writeNewComment(
            @RequestHeader(AUTHORIZATION) final String authorization,
            @RequestBody @Valid final WriteCommentRequestDto writeCommentRequestDto,
            @RequestParam("post_id") final Long postId
    ) {
        Long commentId =
                commentService.writeComment(writeCommentRequestDto, postId, null, authorization);
        return ResponseEntity.created(URI.create("/comments/" + commentId)).body("댓글 작성이 완료되었습니다.");
    }

    @PostMapping("/reply")
    public ResponseEntity<String> replyNewComment(
            @RequestHeader(AUTHORIZATION) final String authorization,
            @RequestBody @Valid WriteCommentRequestDto writeCommentRequestDto,
            @RequestParam("comment_id") final Long commentId
    ) {
        Long replyId =
                commentService.writeComment(writeCommentRequestDto, null, commentId, authorization);
        return ResponseEntity.created(URI.create("/reply/" + replyId)).body("대댓글 작성이 완료되었습니다.");
    }

    @DeleteMapping
    public ResponseEntity<String> deleteOneComment(
            @RequestHeader(AUTHORIZATION) final String authorization,
            @RequestParam("comment_id") final Long commentId
    ) {
        commentService.deleteComment(authorization, commentId);
        return ResponseEntity.ok().body("댓글 삭제가 완료되었습니다.");
    }
}
