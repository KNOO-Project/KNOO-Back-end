package com.woopaca.knoo.comment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.woopaca.knoo.config.jwt.JwtProvider;
import com.woopaca.knoo.controller.dto.auth.SignInUser;
import com.woopaca.knoo.controller.dto.comment.WriteCommentRequestDto;
import com.woopaca.knoo.controller.dto.post.WritePostRequestDto;
import com.woopaca.knoo.entity.Comment;
import com.woopaca.knoo.entity.value.EmailVerify;
import com.woopaca.knoo.entity.value.PostCategory;
import com.woopaca.knoo.entity.User;
import com.woopaca.knoo.repository.CommentRepository;
import com.woopaca.knoo.repository.UserRepository;
import com.woopaca.knoo.service.CommentService;
import com.woopaca.knoo.service.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@WithMockUser
public class CommentLikeTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper mapper;
    @Autowired
    JwtProvider jwtProvider;
    @Autowired
    UserRepository userRepository;
    @Autowired
    PostService postService;
    @Autowired
    CommentService commentService;
    @Autowired
    CommentRepository commentRepository;

    private String authorizationA;
    private String authorizationB;
    private Long postId;
    private Long commentId;

    @BeforeEach
    void beforeEach() {
        User userA = User.builder()
                .username("test")
                .password("password")
                .email("test@test")
                .name("test")
                .emailVerify(EmailVerify.ENABLE)
                .verificationCode("test")
                .joinDate(LocalDateTime.now())
                .build();
        User userB = User.builder()
                .username("test1")
                .password("password1")
                .email("test1@test")
                .name("test1")
                .emailVerify(EmailVerify.ENABLE)
                .verificationCode("test1")
                .joinDate(LocalDateTime.now())
                .build();
        userRepository.save(userA);
        userRepository.save(userB);

        authorizationA = "Bearer " + jwtProvider.createToken(userA, 10);
        authorizationB = "Bearer " + jwtProvider.createToken(userB, 10);

        WritePostRequestDto writePostRequestDto = WritePostRequestDto.builder()
                .postTitle("test")
                .postContent("test")
                .postCategory(PostCategory.FREE)
                .isAnonymous(false)
                .build();

        SignInUser signInUser = SignInUser.builder()
                .id(userA.getId())
                .username(userA.getUsername())
                .build();
        postId = postService.writePost(signInUser, writePostRequestDto);

        WriteCommentRequestDto writeCommentRequestDto =
                new WriteCommentRequestDto("test comment");
        commentId =
                commentService.writeComment(signInUser, writeCommentRequestDto, postId, null);
    }

    @Test
    @DisplayName("댓글 좋아요 - 성공")
    void likeCommentSuccess() throws Exception {
        // given

        // when
        ResultActions resultActions = resultActions(commentId, authorizationA);

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(content().json("{'comment_id': " + commentId + ", 'liked': true, 'likes_count': 1}"));
    }

    @Test
    @DisplayName("댓글 좋아요 취소 - 성공")
    void cancelLikeCommentSuccess() throws Exception {
        // given
        resultActions(commentId, authorizationA);
        Comment comment = commentRepository.findById(commentId).get();

        assert comment.getLikesCount() == 1;

        // when
        ResultActions resultActions = resultActions(commentId, authorizationA);

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(content().json("{'comment_id': " + commentId + ", 'liked': false, 'likes_count': 0}"));
    }

    @Test
    @DisplayName("댓글 좋아요 여러명 - 성공")
    void likeCommentMultipleUserSuccess() throws Exception {
        // given

        // when
        resultActions(commentId, authorizationA);
        ResultActions resultActions = resultActions(commentId, authorizationB);

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(content().json("{'comment_id': " + commentId + ", 'liked': true, 'likes_count': 2}"));
    }

    @Test
    @DisplayName("게시글 좋아요 실패 - 삭제된 댓글")
    void likePostFailDeletedComment() throws Exception {
        // given
        mockMvc.perform(delete("/api/v1/comments")
                        .param("comment_id", String.valueOf(commentId))
                        .header(HttpHeaders.AUTHORIZATION, authorizationA))
                .andDo(print());
        Comment comment = commentRepository.findById(commentId).get();

        assert comment.isDeleted();

        // when
        ResultActions resultActions = resultActions(commentId, authorizationA);

        // then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("댓글 좋아요 실패 - 존재하지 않는 댓글")
    void likePostFailNonexistentComment() throws Exception {
        // given

        // when
        ResultActions resultActions = resultActions(0L, authorizationA);

        // then
        resultActions.andExpect(status().isNotFound());
    }

    private ResultActions resultActions(Long commentId, String authorization) throws Exception {
        return mockMvc.perform(post("/api/v1/comments/likes")
                        .param("comment_id", String.valueOf(commentId))
                        .header(HttpHeaders.AUTHORIZATION, authorization))
                .andDo(print());
    }
}
