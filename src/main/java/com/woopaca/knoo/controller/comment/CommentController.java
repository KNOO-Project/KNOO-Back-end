package com.woopaca.knoo.controller.comment;

import com.woopaca.knoo.controller.comment.dto.WriteCommentRequestDto;
import com.woopaca.knoo.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
