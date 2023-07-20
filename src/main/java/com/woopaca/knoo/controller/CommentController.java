package com.woopaca.knoo.controller;

import com.woopaca.knoo.annotation.SignIn;
import com.woopaca.knoo.controller.dto.auth.SignInUser;
import com.woopaca.knoo.controller.dto.comment.CommentLikeResponseDto;
import com.woopaca.knoo.controller.dto.comment.WriteCommentRequestDto;
import com.woopaca.knoo.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<String> writeNewComment(
            @SignIn final SignInUser signInUser,
            @RequestBody @Valid final WriteCommentRequestDto writeCommentRequestDto,
            @RequestParam("post_id") final Long postId
    ) {
        Long commentId =
                commentService.writeComment(signInUser, writeCommentRequestDto, postId, null);
        return ResponseEntity.created(URI.create("/comments/" + commentId)).body("댓글 작성이 완료되었습니다.");
    }

    @PostMapping("/reply")
    public ResponseEntity<String> replyNewComment(
            @SignIn final SignInUser signInUser,
            @RequestBody @Valid final WriteCommentRequestDto writeCommentRequestDto,
            @RequestParam("comment_id") final Long commentId
    ) {
        Long replyId =
                commentService.writeComment(signInUser, writeCommentRequestDto, null, commentId);
        return ResponseEntity.created(URI.create("/reply/" + replyId)).body("대댓글 작성이 완료되었습니다.");
    }

    @DeleteMapping
    public ResponseEntity<String> deleteOneComment(
            @SignIn final SignInUser signInUser,
            @RequestParam("comment_id") final Long commentId
    ) {
        commentService.deleteComment(signInUser, commentId);
        return ResponseEntity.ok().body("댓글 삭제가 완료되었습니다.");
    }

    @PostMapping("/likes")
    public ResponseEntity<CommentLikeResponseDto> commentLikeOrCancelLike(
            @SignIn final SignInUser signInUser,
            @RequestParam("comment_id") final Long commentId
    ) {
        CommentLikeResponseDto commentLikeResponseDto =
                commentService.changeCommentLike(signInUser, commentId);
        return ResponseEntity.ok().body(commentLikeResponseDto);
    }
}
