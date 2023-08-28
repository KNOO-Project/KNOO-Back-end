package com.woopaca.knoo.post;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.woopaca.knoo.config.jwt.JwtProvider;
import com.woopaca.knoo.controller.dto.auth.SignInUser;
import com.woopaca.knoo.controller.dto.post.WritePostRequestDto;
import com.woopaca.knoo.entity.User;
import com.woopaca.knoo.entity.value.EmailVerify;
import com.woopaca.knoo.entity.value.PostCategory;
import com.woopaca.knoo.repository.UserRepository;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
@WithMockUser
@Transactional
public class PostDetailsTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper mapper;
    @Autowired
    PostService postService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    JwtProvider jwtProvider;

    private String authorization;
    private SignInUser signInUser;

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

        authorization = "Bearer " + jwtProvider.createToken(user, 10);

        signInUser = SignInUser.builder()
                .id(user.getId())
                .username(user.getUsername())
                .build();
    }

    @Test
    @DisplayName("게시글 상세 조회 성공")
    void getPostDetailsSuccess() throws Exception {
        //given
        WritePostRequestDto writePostRequestDto = WritePostRequestDto.builder()
                .postTitle("test title")
                .postContent("test content")
                .postCategory(PostCategory.FREE)
                .isAnonymous(true)
                .build();

        Long postId = postService.writePost(signInUser, writePostRequestDto);

        //when
        ResultActions resultActions = resultActions(postId);

        //then
        resultActions.andExpect(status().isOk());

    }

    private ResultActions resultActions(Long postId) throws Exception {
        return mockMvc.perform(get("/api/posts/{post_id}", postId)
                        .header(HttpHeaders.AUTHORIZATION, authorization))
                .andDo(print());
    }
}
