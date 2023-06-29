package com.woopaca.knoo.concurrency;

import com.woopaca.knoo.config.jwt.JwtProvider;
import com.woopaca.knoo.controller.dto.auth.SignInUser;
import com.woopaca.knoo.controller.dto.comment.WriteCommentRequestDto;
import com.woopaca.knoo.repository.UserRepository;
import com.woopaca.knoo.service.CommentService;
import com.woopaca.knoo.service.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@Execution(ExecutionMode.CONCURRENT)
@SpringBootTest
public class ConcurrencyTest {

    @Autowired
    JwtProvider jwtProvider;
    @Autowired
    CommentService commentService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    PostService postService;

    private SignInUser signInUser;

    @BeforeEach
    void beforeEach() {
        signInUser = new SignInUser(1L, "test1234");
    }

    @Rollback(value = false)
    @DisplayName("동시성 문제 테스트 - 댓글 작성 1")
    @Test
    void concurrencyTest1() throws Exception {
        // given
        WriteCommentRequestDto writeCommentRequestDto = new WriteCommentRequestDto("test");

        // when
        Long writtenCommentId =
                commentService.writeComment(signInUser, writeCommentRequestDto, 1L, null);

        // then
        assertThat(writtenCommentId).isNotNull();
    }

    @Rollback(value = false)
    @DisplayName("동시성 문제 테스트 - 댓글 작성 2")
    @Test
    void concurrencyTest2() throws Exception {
        // given
        WriteCommentRequestDto writeCommentRequestDto = new WriteCommentRequestDto("test");

        // when
        Long writtenCommentId =
                commentService.writeComment(signInUser, writeCommentRequestDto, 1L, null);

        // then
        assertThat(writtenCommentId).isNotNull();
    }

    @Rollback(value = false)
    @DisplayName("동시성 문제 테스트 - 댓글 작성 3")
    @Test
    void concurrencyTest3() throws Exception {
        // given
        WriteCommentRequestDto writeCommentRequestDto = new WriteCommentRequestDto("test");

        // when
        Long writtenCommentId =
                commentService.writeComment(signInUser, writeCommentRequestDto, 1L, null);

        // then
        assertThat(writtenCommentId).isNotNull();
    }
}
