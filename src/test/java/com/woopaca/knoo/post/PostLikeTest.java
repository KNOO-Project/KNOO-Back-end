package com.woopaca.knoo.post;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.woopaca.knoo.config.jwt.JwtProvider;
import com.woopaca.knoo.entity.EmailVerify;
import com.woopaca.knoo.entity.Post;
import com.woopaca.knoo.entity.PostCategory;
import com.woopaca.knoo.entity.User;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser
@Transactional
public class PostLikeTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper mapper;
    @Autowired
    PostRepository postRepository;
    @Autowired
    PostService postService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    JwtProvider jwtProvider;

    private Long postId;
    private String authorizationA;
    private String authorizationB;

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

        Post post = Post.builder()
                .postTitle("test")
                .postContent("test")
                .postCategory(PostCategory.FREE)
                .postDate(LocalDateTime.now())
                .build();
        post.writtenBy(userA);
        postRepository.save(post);
        postId = post.getId();
    }

    @Test
    @DisplayName("게시글 좋아요 - 성공")
    void likesPostSuccess() throws Exception {
        // given

        // when
        ResultActions resultActions = resultActions(postId, authorizationA);

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(content().json("{'post_id': " + postId + ",'liked': true, 'likes': 1}"));
    }

    @Test
    @DisplayName("게시글 좋아요 취소 - 성공")
    void unlikesPostSuccess() throws Exception {
        // given
        resultActions(postId, authorizationA);
        Post post = postRepository.findById(postId).get();

        assert post.getLikesCount() == 1;

        // when
        ResultActions resultActions = resultActions(postId, authorizationA);

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(content().json("{'post_id': " + postId + ",'liked': false, 'likes': 0}"));
    }

    @Test
    @DisplayName("게시글 좋아요 여러명 - 성공")
    void likesPostMultipleUserSuccess() throws Exception {
        // given

        // when
        resultActions(postId, authorizationA);
        ResultActions resultActions = resultActions(postId, authorizationB);

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(content().json("{'post_id': " + postId + ",'liked': true, 'likes': 2}"));
    }

    @Test
    @DisplayName("게시글 좋아요 실패 - 존재하지 않는 게시글")
    void likesPostFail() throws Exception {
        // given

        // when
        ResultActions resultActions = resultActions(0L, authorizationA);

        // then
        resultActions.andExpect(status().isNotFound());
    }

    private ResultActions resultActions(Long postId, String authorization) throws Exception {
        return mockMvc.perform(post("/api/v1/posts/likes")
                        .param("post_id", String.valueOf(postId))
                        .header(HttpHeaders.AUTHORIZATION, authorization))
                .andDo(print());
    }
}
