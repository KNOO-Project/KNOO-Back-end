package com.woopaca.knoo.concurrency;

import com.woopaca.knoo.controller.dto.auth.SignInUser;
import com.woopaca.knoo.controller.dto.comment.WriteCommentRequestDto;
import com.woopaca.knoo.controller.dto.post.WritePostRequestDto;
import com.woopaca.knoo.entity.Post;
import com.woopaca.knoo.entity.User;
import com.woopaca.knoo.entity.value.EmailVerify;
import com.woopaca.knoo.entity.value.PostCategory;
import com.woopaca.knoo.repository.PostRepository;
import com.woopaca.knoo.repository.UserRepository;
import com.woopaca.knoo.service.CommentService;
import com.woopaca.knoo.service.PostService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

@Transactional(isolation = Isolation.REPEATABLE_READ)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Execution(ExecutionMode.CONCURRENT)
@SpringBootTest
public class ConcurrencyTest {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    CommentService commentService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    PostService postService;
    @Autowired
    PostRepository postRepository;

    private SignInUser signInUser;
    private Long postId;

    @BeforeAll
    void beforeAll() {
        User user = User.builder()
                .username("test")
                .password("password")
                .email("test@test")
                .name("test")
                .emailVerify(EmailVerify.ENABLE)
                .verificationCode("test")
                .joinDate(LocalDateTime.now())
                .build();
        userRepository.save(user);
        signInUser = new SignInUser(user.getId(), user.getUsername());

        WritePostRequestDto writePostRequestDto = WritePostRequestDto.builder()
                .postTitle("test")
                .postContent("test")
                .postCategory(PostCategory.FREE)
                .isAnonymous(false)
                .build();
        postId = postService.writePost(signInUser, writePostRequestDto);
    }

    @Rollback(value = false)
    @DisplayName("동시성 문제 테스트 - 댓글 작성 1")
    @Test
    void concurrencyTest1() throws Exception {
        // given
        WriteCommentRequestDto writeCommentRequestDto = new WriteCommentRequestDto("test");

        // when
        Long writtenCommentId =
                commentService.writeComment(signInUser, writeCommentRequestDto, postId, null);

        // then
        assertThat(writtenCommentId).isNotNull();
        Optional<Post> optionalPost = postRepository.findById(postId);
        if (optionalPost.isEmpty()) {
            fail("post is null");
        }

        Post post = optionalPost.get();
        log.info("post.getCommentsCount() = {}", post.getCommentsCount());
    }

    @Rollback(value = false)
    @DisplayName("동시성 문제 테스트 - 댓글 작성 2")
    @Test
    void concurrencyTest2() throws Exception {
        // given
        WriteCommentRequestDto writeCommentRequestDto = new WriteCommentRequestDto("test");

        // when
        Long writtenCommentId =
                commentService.writeComment(signInUser, writeCommentRequestDto, postId, null);

        // then
        assertThat(writtenCommentId).isNotNull();
        Optional<Post> optionalPost = postRepository.findById(postId);
        if (optionalPost.isEmpty()) {
            fail("post is null");
        }

        Post post = optionalPost.get();
        log.info("post.getCommentsCount() = {}", post.getCommentsCount());
    }

    @Rollback(value = false)
    @DisplayName("동시성 문제 테스트 - 댓글 작성 3")
    @Test
    void concurrencyTest3() throws Exception {
        // given
        WriteCommentRequestDto writeCommentRequestDto = new WriteCommentRequestDto("test");

        // when
        Long writtenCommentId =
                commentService.writeComment(signInUser, writeCommentRequestDto, postId, null);

        // then
        assertThat(writtenCommentId).isNotNull();
        Optional<Post> optionalPost = postRepository.findById(postId);
        if (optionalPost.isEmpty()) {
            fail("post is null");
        }

        Post post = optionalPost.get();
        log.info("post.getCommentsCount() = {}", post.getCommentsCount());
    }
}
