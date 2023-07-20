package com.woopaca.knoo.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.woopaca.knoo.config.jwt.JwtProvider;
import com.woopaca.knoo.controller.dto.auth.SignInUser;
import com.woopaca.knoo.controller.dto.post.SearchCondition;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@AutoConfigureMockMvc
@WithMockUser
@SpringBootTest
public class SearchUserScrapListTest {

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
    private String authorization;
    private SignInUser signInUser;

    @BeforeEach
    void beforeEach() {
        User user = createUser("test");
        userRepository.save(user);
        authorization = "Bearer " + jwtProvider.createToken(user, 10);
        signInUser = new SignInUser(user.getId(), user.getUsername());

        for (int i = 1; i <= 10; i++) {
            Post post = createPost(i + "번째 게시글", PostCategory.FREE);
            post.writtenBy(user);
            postRepository.save(post);

            if (i % 2 == 0) {
                postService.changePostScrap(signInUser, post.getId());
            }
        }
    }

    @Test
    @DisplayName("스크랩 목록에서 '제목+본문' 조건으로 검색 - 성공")
    void searchPostsInUserScrapSuccess() throws Exception {
        // given

        // when
        ResultActions resultActionsA = resultActions(SearchCondition.ALL, "10", 1);
        ResultActions resultActionsB = resultActions(SearchCondition.ALL, "11", 1);

        // then
        resultActionsA.andExpect(status().isOk())
                .andExpect(jsonPath("posts.[0].post_title").value("Title 10번째 게시글"))
                .andExpect(content().json("{posts: [{}], total_pages: 1}"));
        resultActionsB.andExpect(status().isOk())
                .andExpect(content().json("{posts: [], total_pages: 0}"));
    }

    @Test
    @DisplayName("스크랩 목록에서 '제목' 조건으로 검색 - 성공")
    void searchPostsByContentInUserScrapSuccess() throws Exception {
        // given

        // when
        ResultActions resultActionsA = resultActions(SearchCondition.CONTENT, "onten", 1);
        ResultActions resultActionsB = resultActions(SearchCondition.CONTENT, "itl", 1);

        // then
        resultActionsA.andExpect(status().isOk())
                .andExpect(jsonPath("posts.[0].post_title").value("Title 10번째 게시글"))
                .andExpect(content().json("{posts: [{}, {}, {}, {}, {}], total_pages: 1}"));
        resultActionsB.andExpect(status().isOk())
                .andExpect(content().json("{posts: [], total_pages: 0}"));
    }

    @Test
    @DisplayName("스크랩 목록에서 '본문' 조건으로 검색 - 성공")
    void searchPostsByTitleInUserScrapSuccess() throws Exception {
        // given

        // when
        ResultActions resultActionsA = resultActions(SearchCondition.TITLE, "itl", 1);
        ResultActions resultActionsB = resultActions(SearchCondition.TITLE, "onten", 1);

        // then
        resultActionsA.andExpect(status().isOk())
                .andExpect(jsonPath("posts.[0].post_title").value("Title 10번째 게시글"))
                .andExpect(content().json("{posts: [{}, {}, {}, {}, {}], total_pages: 1}"));
        resultActionsB.andExpect(status().isOk())
                .andExpect(content().json("{posts: [], total_pages: 0}"));
    }

    @Test
    @DisplayName("게시글 검색 실패 - 검색어 글자수 부족")
    void searchPostFailInsufficientKeywordCharacterCount() throws Exception {
        // given

        // when
        ResultActions resultActions = resultActions(SearchCondition.ALL, "1", 1);

        // then
        resultActions.andExpect(status().isBadRequest())
                .andExpect(jsonPath("error_code").value("KN101"));
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

    private static Post createPost(String signature, PostCategory postCategory) {
        return Post.builder()
                .postTitle("Title " + signature)
                .postContent("Content " + signature)
                .postCategory(postCategory)
                .postDate(LocalDateTime.now())
                .build();
    }

    private ResultActions resultActions(SearchCondition searchCondition, String keyword, int page)
            throws Exception {
        return mockMvc.perform(get("/api/users/scraps/search")
                        .header(HttpHeaders.AUTHORIZATION, authorization)
                        .param("condition", searchCondition.getConditionName())
                        .param("keyword", keyword)
                        .param("page", String.valueOf(page)))
                .andDo(print());
    }
}
