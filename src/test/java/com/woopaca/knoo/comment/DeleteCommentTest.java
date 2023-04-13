package com.woopaca.knoo.comment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.woopaca.knoo.config.jwt.JwtProvider;
import com.woopaca.knoo.controller.dto.auth.SignInUser;
import com.woopaca.knoo.controller.dto.comment.WriteCommentRequestDto;
import com.woopaca.knoo.controller.dto.post.WritePostRequestDto;
import com.woopaca.knoo.entity.Comment;
import com.woopaca.knoo.entity.EmailVerify;
import com.woopaca.knoo.entity.PostCategory;
import com.woopaca.knoo.entity.User;
import com.woopaca.knoo.repository.CommentRepository;
import com.woopaca.knoo.repository.UserRepository;
import com.woopaca.knoo.service.CommentService;
import com.woopaca.knoo.service.PostService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@WithMockUser
public class DeleteCommentTest {

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

    Logger log = LoggerFactory.getLogger(getClass());

    @BeforeEach
    void beforeEach() {
        User userA = User.builder()
                .username("test")
                .password("password")
                .email("test@test")
                .name("test")
                .emailVerify(EmailVerify.ENABLE)
                .verificationCode("test")
                .joinDate(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm").format(LocalDateTime.now()))
                .build();
        User userB = User.builder()
                .username("test1")
                .password("password1")
                .email("test1@test")
                .name("test1")
                .emailVerify(EmailVerify.ENABLE)
                .verificationCode("test1")
                .joinDate(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm").format(LocalDateTime.now()))
                .build();
        userRepository.save(userA);
        userRepository.save(userB);

        log.info("userA.id = {}", userA.getId());

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
    @DisplayName("댓글 삭제 - 성공")
    void deleteCommentSuccess() throws Exception {
        //given

        //when
        ResultActions resultActions = resultActions(authorizationA, commentId);

        //then
        resultActions.andExpect(status().isOk())
                .andExpect(content().string("댓글 삭제가 완료되었습니다."));

        Comment comment = commentRepository.findById(commentId).orElse(null);
        assert comment != null;
        Assertions.assertThat(comment.isDeleted()).isEqualTo(true);
    }

    @Test
    @DisplayName("댓글 삭제 실패 - 유효하지 않은 회원")
    void deleteCommentFailInvalidUser() throws Exception {
        //given

        //when
        ResultActions resultActions = resultActions(authorizationB, commentId);

        //then
        resultActions.andExpect(status().isUnauthorized());

    }

    private ResultActions resultActions(String authorization, Long commentId) throws Exception {
        return mockMvc.perform(delete("/api/v1/comments")
                        .param("comment_id", String.valueOf(commentId))
                        .header(HttpHeaders.AUTHORIZATION, authorization))
                .andDo(print());
    }
}
