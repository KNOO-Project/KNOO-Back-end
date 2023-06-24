package com.woopaca.knoo.post;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.woopaca.knoo.config.jwt.JwtProvider;
import com.woopaca.knoo.controller.dto.auth.SignInUser;
import com.woopaca.knoo.controller.dto.post.WritePostRequestDto;
import com.woopaca.knoo.entity.User;
import com.woopaca.knoo.entity.value.EmailVerify;
import com.woopaca.knoo.entity.value.PostCategory;
import com.woopaca.knoo.exception.post.impl.PostNotFoundException;
import com.woopaca.knoo.repository.UserRepository;
import com.woopaca.knoo.service.PostService;
import org.junit.jupiter.api.Assertions;
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
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@WithMockUser
public class DeletePostTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    JwtProvider jwtProvider;
    @Autowired
    UserRepository userRepository;
    @Autowired
    PostService postService;

    private String authorizationA;
    private String authorizationB;
    private Long postId;
    private SignInUser signInUser;

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

        signInUser = SignInUser.builder()
                .id(userA.getId())
                .username(userA.getUsername())
                .build();
        postId = postService.writePost(signInUser, writePostRequestDto);
    }

    @Test
    @DisplayName("게시글 삭제 - 성공")
    void deletePostSuccess() throws Exception {
        //given

        //when
        ResultActions resultActions = resultActions(authorizationA, postId);

        //then
        resultActions.andExpect(status().isOk())
                .andExpect(content().string("게시글 삭제가 완료되었습니다."));
        Assertions.assertThrows(PostNotFoundException.class, () ->
                postService.postDetails(signInUser, postId));

    }

    @Test
    @DisplayName("게시글 삭제 실패 - 유효하지 않은 회원")
    void deletePostFailInvalidUser() throws Exception {
        //given

        //when
        ResultActions resultActions = resultActions(authorizationB, postId);

        //then
        resultActions.andExpect(status().isUnauthorized());

    }

    @Test
    @DisplayName("게시글 삭제 실패 - 존재하지 않는 게시글")
    void deletePostFailPostNonexistent() throws Exception {
        //given

        //when
        ResultActions resultActions = resultActions(authorizationA, 0L);

        //then
        resultActions.andExpect(status().isNotFound());

    }

    private ResultActions resultActions(String authorization, Long postId) throws Exception {
        return mockMvc.perform(delete("/api/v1/posts")
                        .param("post_id", String.valueOf(postId))
                        .header(HttpHeaders.AUTHORIZATION, authorization))
                .andDo(print());
    }
}
