/*
package com.woopaca.knoo.concurrency;

import com.woopaca.knoo.controller.dto.auth.SignInUser;
import com.woopaca.knoo.controller.dto.comment.WriteCommentRequestDto;
import com.woopaca.knoo.controller.dto.post.WritePostRequestDto;
import com.woopaca.knoo.entity.Comment;
import com.woopaca.knoo.entity.Post;
import com.woopaca.knoo.entity.User;
import com.woopaca.knoo.entity.value.EmailVerify;
import com.woopaca.knoo.entity.value.PostCategory;
import com.woopaca.knoo.exception.user.impl.UserNotFoundException;
import com.woopaca.knoo.repository.PostRepository;
import com.woopaca.knoo.repository.UserRepository;
import com.woopaca.knoo.service.CommentService;
import com.woopaca.knoo.service.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertAll;

@Transactional
@SpringBootTest
public class NewConcurrencyTest {

    public static final int NUMBER_OF_COMMENTS = 3;

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

    @BeforeEach
    void beforeEach() {
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

    @DisplayName("동시성 문제 테스트")
    @Test
    void concurrencyTest() throws Exception {
        // given
        WriteCommentRequestDto writeCommentRequestDto = new WriteCommentRequestDto("test");

        User user = userRepository.findByUsername(signInUser.getUsername())
                .orElseThrow(UserNotFoundException::new);

        // when
        ExecutorService executorService = Executors.newFixedThreadPool(NUMBER_OF_COMMENTS);
        for (int i = 0; i < NUMBER_OF_COMMENTS; i++) {
            executorService.execute(() ->
                    commentService.writeComment(signInUser, writeCommentRequestDto, postId, null));
        }

        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.MINUTES);

        // then
        Optional<Post> optionalPost = postRepository.findById(postId);
        if (optionalPost.isEmpty()) {
            fail("post is null");
        }

        Post post = optionalPost.get();
        log.info("post.getCommentsCount() = {}", post.getCommentsCount());

        List<Comment> comments = post.getComments();
        log.info("comments.size() = {}", comments.size());

        assertAll(
                () -> assertThat(post.getCommentsCount()).isEqualTo(NUMBER_OF_COMMENTS),
                () -> assertThat(comments.size()).isEqualTo(NUMBER_OF_COMMENTS)
        );
    }
}
*/
