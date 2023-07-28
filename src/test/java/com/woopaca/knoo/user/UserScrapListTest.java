package com.woopaca.knoo.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.woopaca.knoo.config.jwt.JwtProvider;
import com.woopaca.knoo.controller.dto.auth.SignInUser;
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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser
@Transactional
public class UserScrapListTest {

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
    @DisplayName("스크랩 리스트 조회 - 성공")
    void getScrapListSuccess() throws Exception {
        // given
        postService.changePostScrap(signInUser, postAId);
        postService.changePostScrap(signInUser, postBId);

        // when
        ResultActions resultActions = resultActions(authorizationA, 1);

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(content().json("{'posts': [{}, {}], 'total_pages': 1}"));
    }

    @Test
    @DisplayName("빈 스크랩 리스트 조회 - 성공")
    void getEmptyScrapListSuccess() throws Exception {
        // given

        // when
        ResultActions resultActions = resultActions(authorizationA, 1);

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(content().json("{'posts': [], 'total_pages': 0}"));
    }

    @Test
    @DisplayName("스크랩 리스트 조회 실패 - 페이지 수 초과")
    void getScrapListFailOutOfPages() throws Exception {
        // given
        postService.changePostScrap(signInUser, postAId);
        postService.changePostScrap(signInUser, postBId);

        // when
        ResultActions resultActions = resultActions(authorizationA, 100);

        // then
        resultActions.andExpect(status().isBadRequest())
                .andExpect(content().json("{'error_code': 'KN303'}"));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1})
    @DisplayName("스크랩 리스트 조회 실패 - 유효하지 않은 페이지")
    void getScrapListFailInvalidPage(int page) throws Exception {
        // given

        // when
        ResultActions resultActions = resultActions(authorizationA, page);

        // then
        resultActions.andExpect(status().isBadRequest())
                .andExpect(content().json("{'error_code': 'KN304'}"));
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

    private ResultActions resultActions(String authorization, int page) throws Exception {
        return mockMvc.perform(get("/api/users/scraps")
                        .param("page", String.valueOf(page))
                        .header(HttpHeaders.AUTHORIZATION, authorization))
                .andDo(print());
    }
}
