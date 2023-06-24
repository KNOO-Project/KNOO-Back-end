package com.woopaca.knoo.post;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.woopaca.knoo.config.jwt.JwtProvider;
import com.woopaca.knoo.controller.dto.auth.SignInUser;
import com.woopaca.knoo.controller.dto.post.UpdatePostRequestDto;
import com.woopaca.knoo.controller.dto.post.WritePostRequestDto;
import com.woopaca.knoo.entity.Post;
import com.woopaca.knoo.entity.User;
import com.woopaca.knoo.entity.value.EmailVerify;
import com.woopaca.knoo.entity.value.PostCategory;
import com.woopaca.knoo.repository.PostRepository;
import com.woopaca.knoo.repository.UserRepository;
import com.woopaca.knoo.service.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@WithMockUser
public class UpdatePostTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper mapper;
    @Autowired
    UserRepository userRepository;
    @Autowired
    PostService postService;
    @Autowired
    PostRepository postRepository;
    @Autowired
    JwtProvider jwtProvider;

    private String authorizationA;
    private String authorizationB;
    private Long postId;

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
    }

    @Test
    @DisplayName("게시글 수정 - 성공")
    void updatePostSuccess() throws Exception {
        //given
        UpdatePostRequestDto updatePostRequestDto = UpdatePostRequestDto.builder()
                .postTitle("update test")
                .postContent("update test")
                .isAnonymous(true)
                .build();

        //when
        ResultActions resultActions = resultActions(updatePostRequestDto, authorizationA, postId);
        Post updatedPost = postRepository.findById(postId).orElse(null);

        //then
        resultActions.andExpect(status().isOk())
                .andExpect(content().string("게시글 수정이 완료되었습니다."));
        assertThat(updatedPost.getPostTitle()).isEqualTo("update test");
        assertThat(updatedPost.isAnonymous()).isEqualTo(true);
    }

    @Test
    @DisplayName("게시글 수정 실패 - 유효하지 않은 회원")
    void updatePostFailInvalidUser() throws Exception {
        //given
        UpdatePostRequestDto updatePostRequestDto = UpdatePostRequestDto.builder()
                .postTitle("update test")
                .postContent("update test")
                .isAnonymous(true)
                .build();

        //when
        ResultActions resultActions = resultActions(updatePostRequestDto, authorizationB, postId);

        //then
        resultActions.andExpect(status().isUnauthorized());

    }

    @Test
    @DisplayName("게시글 수정 실패 - 공백")
    void updatePostFailBlank() throws Exception {
        //given
        UpdatePostRequestDto updatePostRequestDto = UpdatePostRequestDto.builder()
                .postTitle("    ")
                .postContent("update test")
                .isAnonymous(true)
                .build();

        //when
        ResultActions resultActions = resultActions(updatePostRequestDto, authorizationA, postId);

        //then
        resultActions.andExpect(status().isBadRequest());

    }

    @Test
    @DisplayName("게시글 수정 실패 - 존재하지 않는 게시글")
    void updatePostFailPostNonexistent() throws Exception {
        //given
        UpdatePostRequestDto updatePostRequestDto = UpdatePostRequestDto.builder()
                .postTitle("update test")
                .postContent("update test")
                .isAnonymous(true)
                .build();

        //when
        ResultActions resultActions = resultActions(updatePostRequestDto, authorizationA, 0L);

        //then
        resultActions.andExpect(status().isNotFound());

    }

    private ResultActions resultActions(UpdatePostRequestDto updatePostRequestDto, String authorization, Long postId) throws Exception {
        return mockMvc.perform(patch("/api/v1/posts")
                        .param("post_id", String.valueOf(postId))
                        .header(HttpHeaders.AUTHORIZATION, authorization)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(updatePostRequestDto)))
                .andDo(print());
    }
}
