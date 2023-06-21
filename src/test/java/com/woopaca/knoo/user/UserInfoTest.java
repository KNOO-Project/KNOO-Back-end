package com.woopaca.knoo.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.woopaca.knoo.config.jwt.JwtProvider;
import com.woopaca.knoo.controller.dto.auth.SignInUser;
import com.woopaca.knoo.controller.dto.comment.WriteCommentRequestDto;
import com.woopaca.knoo.controller.dto.user.UserInfoResponseDto;
import com.woopaca.knoo.entity.Post;
import com.woopaca.knoo.entity.User;
import com.woopaca.knoo.entity.value.EmailVerify;
import com.woopaca.knoo.entity.value.PostCategory;
import com.woopaca.knoo.repository.PostRepository;
import com.woopaca.knoo.repository.UserRepository;
import com.woopaca.knoo.service.CommentService;
import com.woopaca.knoo.service.PostService;
import com.woopaca.knoo.service.UserService;
import org.assertj.core.api.Assertions;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser
@Transactional
public class UserInfoTest {

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
    CommentService commentService;
    @Autowired
    UserService userService;
    @Autowired
    JwtProvider jwtProvider;

    private Long postAId;
    private Long postBId;
    private String authorizationA;
    private SignInUser signInUser;

    @BeforeEach
    void beforeEach() {
        User userA = createUser("test");
        userRepository.save(userA);
        authorizationA = "Bearer " + jwtProvider.createToken(userA, 10);
        signInUser = new SignInUser(userA.getId(), userA.getUsername());

        Post postA = createPost();
        postA.writtenBy(userA);
        postRepository.save(postA);
        postAId = postA.getId();

        Post postB = createPost();
        postB.writtenBy(userA);
        postRepository.save(postB);
        postBId = postB.getId();
    }

    @Test
    @DisplayName("회원 프로필 조회")
    void userProfileTest() throws Exception {
        // given
        WriteCommentRequestDto writeCommentRequestDto = new WriteCommentRequestDto("Test comment");
        commentService.writeComment(signInUser, writeCommentRequestDto, postAId, null);
        commentService.writeComment(signInUser, writeCommentRequestDto, postBId, null);
        postService.changePostLike(signInUser, postAId);
        postService.changePostLike(signInUser, postBId);

        // when
        ResultActions resultActions = resultActions(authorizationA);

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("name").value("test"))
                .andExpect(jsonPath("email").value("test@test"))
                .andExpect(jsonPath("write_posts").isArray())
                .andExpect(jsonPath("comment_posts").isArray())
                .andExpect(jsonPath("like_posts").isArray());

        UserInfoResponseDto userInfoResponseDto = userService.userInfo(signInUser);
        Assertions.assertThat(userInfoResponseDto.getWritePosts().size()).isEqualTo(2);
        Assertions.assertThat(userInfoResponseDto.getCommentPosts().size()).isEqualTo(2);
        Assertions.assertThat(userInfoResponseDto.getLikePosts().size()).isEqualTo(2);
    }

    private static User createUser(String signature) {
        return User.builder()
                .username(signature)
                .password("password")
                .email(signature + "@test")
                .name(signature)
                .emailVerify(EmailVerify.ENABLE)
                .verificationCode(signature)
                .joinDate(LocalDateTime.now())
                .build();
    }

    private static Post createPost() {
        return Post.builder()
                .postTitle("test")
                .postContent("test")
                .postCategory(PostCategory.FREE)
                .postDate(LocalDateTime.now())
                .build();
    }

    private ResultActions resultActions(String authorization) throws Exception {
        return mockMvc.perform(get("/api/v1/users")
                        .header(HttpHeaders.AUTHORIZATION, authorization))
                .andDo(print());
    }
}
