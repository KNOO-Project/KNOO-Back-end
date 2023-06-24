package com.woopaca.knoo.post;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.woopaca.knoo.config.jwt.JwtProvider;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@WithMockUser
public class SearchPostTest {

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

    private User user;

    @BeforeEach
    void beforeEach() {
        user = createUser("test");
        userRepository.save(user);
    }

    @Test
    @DisplayName("모든 카테고리에서 '제목+본문' 조건으로 검색 - 성공")
    void searchPostInAllCategorySuccess() throws Exception {
        // given
        for (int i = 1; i <= 10; i++) {
            PostCategory[] values = PostCategory.values();
            int length = values.length;
            int index = (int) (Math.random() * length);
            Post post = createPost(i + "번째 게시글", values[index]);
            post.writtenBy(user);
            postRepository.save(post);
        }

        // when
        ResultActions resultActions = resultActions(null, SearchCondition.ALL, "10", 1);

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("posts.[0].post_title").value("Title 10번째 게시글"))
                .andExpect(jsonPath("posts.[0].post_content").value("Content 10번째 게시글"))
                .andExpect(content().json("{posts: [{}], total_pages: 1}"));
    }

    @Test
    @DisplayName("모든 카테고리에서 '제목' 조건으로 검색 - 성공")
    void searchPostByTitleInAllCategoriesSuccess() throws Exception {
        // given
        for (int i = 1; i <= 10; i++) {
            PostCategory[] values = PostCategory.values();
            int length = values.length;
            int index = (int) (Math.random() * length);
            Post post = createPost(i + "번째 게시글", values[index]);
            post.writtenBy(user);
            postRepository.save(post);
        }

        // when
        ResultActions resultActionsA = resultActions(null, SearchCondition.TITLE, "itl", 1);
        ResultActions resultActionsB = resultActions(null, SearchCondition.TITLE, "onten", 1);

        // then
        resultActionsA.andExpect(status().isOk())
                .andExpect(jsonPath("posts.[0].post_title").value("Title 10번째 게시글"))
                .andExpect(jsonPath("posts.[0].post_content").value("Content 10번째 게시글"))
                .andExpect(content()
                        .json("{posts: [{}, {}, {}, {}, {}, {}, {}, {}, {}, {}], total_pages: 1}"));
        resultActionsB.andExpect(status().isOk())
                .andExpect(content().json("{posts: [], total_pages: 0}"));
    }

    @Test
    @DisplayName("모든 카테고리에서 '본문' 조건으로 검색 - 성공")
    void searchPostByContentInAllCategoriesSuccess() throws Exception {
        // given
        for (int i = 1; i <= 10; i++) {
            PostCategory[] values = PostCategory.values();
            int length = values.length;
            int index = (int) (Math.random() * length);
            Post post = createPost(i + "번째 게시글", values[index]);
            post.writtenBy(user);
            postRepository.save(post);
        }

        // when
        ResultActions resultActionsA = resultActions(null, SearchCondition.CONTENT, "onten", 1);
        ResultActions resultActionsB = resultActions(null, SearchCondition.CONTENT, "itl", 1);

        // then
        resultActionsA.andExpect(status().isOk())
                .andExpect(jsonPath("posts.[0].post_title").value("Title 10번째 게시글"))
                .andExpect(jsonPath("posts.[0].post_content").value("Content 10번째 게시글"))
                .andExpect(content()
                        .json("{posts: [{}, {}, {}, {}, {}, {}, {}, {}, {}, {}], total_pages: 1}"));
        resultActionsB.andExpect(status().isOk())
                .andExpect(content().json("{posts: [], total_pages: 0}"));
    }

    @Test
    @DisplayName("특정 카테고리에서 '제목+본문' 조건으로 검색 - 성공")
    void searchPostInSpecificCategorySuccess() throws Exception {
        // given
        PostCategory[] values = PostCategory.values();
        int length = values.length;
        for (int i = 1; i <= length * 2; i++) {
            Post post = createPost(i + "번째 게시글", values[(i - 1) % length]);
            post.writtenBy(user);
            postRepository.save(post);
        }

        // when
        ResultActions resultActions = resultActions(PostCategory.FREE, SearchCondition.ALL, "번째", 1);

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("posts.[0].post_category").value("free"))
                .andExpect(content().json("{posts: [{}, {}], total_pages: 1}"));
    }

    @Test
    @DisplayName("특정 카테고리에서 '제목' 조건으로 검색 - 성공")
    void searchPostByTitleInSpecificCategorySuccess() throws Exception {
        // given
        PostCategory[] values = PostCategory.values();
        int length = values.length;
        for (int i = 1; i <= length * 2; i++) {
            Post post = createPost(i + "번째 게시글", values[(i - 1) % length]);
            post.writtenBy(user);
            postRepository.save(post);
        }

        // when
        ResultActions resultActionsA = resultActions(PostCategory.FREE, SearchCondition.TITLE, "itl", 1);
        ResultActions resultActionsB = resultActions(PostCategory.FREE, SearchCondition.TITLE, "onten", 1);

        // then
        resultActionsA.andExpect(status().isOk())
                .andExpect(jsonPath("posts.[0].post_category").value("free"))
                .andExpect(content().json("{posts: [{}, {}], total_pages: 1}"));
        resultActionsB.andExpect(status().isOk())
                .andExpect(content().json("{posts: [], total_pages: 0}"));
    }

    @Test
    @DisplayName("특정 카테고리에서 '본문' 조건으로 검색 - 성공")
    void searchPostByContentInSpecificCategorySuccess() throws Exception {
        // given
        PostCategory[] values = PostCategory.values();
        int length = values.length;
        for (int i = 1; i <= length * 2; i++) {
            Post post = createPost(i + "번째 게시글", values[(i - 1) % length]);
            post.writtenBy(user);
            postRepository.save(post);
        }

        // when
        ResultActions resultActionsA = resultActions(PostCategory.FREE, SearchCondition.CONTENT, "onten", 1);
        ResultActions resultActionsB = resultActions(PostCategory.FREE, SearchCondition.CONTENT, "itl", 1);

        // then
        resultActionsA.andExpect(status().isOk())
                .andExpect(jsonPath("posts.[0].post_category").value("free"))
                .andExpect(content().json("{posts: [{}, {}], total_pages: 1}"));
        resultActionsB.andExpect(status().isOk())
                .andExpect(content().json("{posts: [], total_pages: 0}"));
    }

    @Test
    @DisplayName("게시글 검색 실패 - 페이지 수 초과")
    void searchPostFailOutOfPages() throws Exception {
        // given
        PostCategory[] values = PostCategory.values();
        int length = values.length;
        for (int i = 1; i <= length * 2; i++) {
            Post post = createPost(i + "번째 게시글", values[(i - 1) % length]);
            post.writtenBy(user);
            postRepository.save(post);
        }

        // when
        ResultActions resultActionsA = resultActions(null, SearchCondition.ALL, "onten", 100);

        // then
        resultActionsA.andExpect(status().isBadRequest())
                .andExpect(jsonPath("message").value("페이지 수를 초과하였습니다."));
    }

    @Test
    @DisplayName("게시글 검색 실패 - 유효하지 않은 페이지")
    void searchPostFailInvalidPage() throws Exception {
        // given

        // when
        ResultActions resultActionsA = resultActions(null, SearchCondition.ALL, "onten", 0);

        // then
        resultActionsA.andExpect(status().isBadRequest())
                .andExpect(jsonPath("message").value("유효하지 않은 페이지입니다."));
    }

    @Test
    @DisplayName("게시글 검색 실패 - 유효하지 않은 검색 조건")
    void searchPostFailInvalidSearchCondition() throws Exception {
        // given

        // when
        ResultActions resultActions = mockMvc.perform(get("/api/v1/posts/search")
                        .param("condition", "invalid")
                        .param("keyword", "10")
                        .param("page", "1"))
                .andDo(print());

        // then
        resultActions.andExpect(status().isBadRequest())
                .andExpect(jsonPath("error_code").value("KN101"));
    }

    @Test
    @DisplayName("게시글 검색 실패 - 검색어 글자수 부족")
    void searchPostFailInsufficientKeywordCharacterCount() throws Exception {
        // given

        // when
        ResultActions resultActions = resultActions(null, SearchCondition.ALL, "1", 1);

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

    private ResultActions resultActions(PostCategory postCategory, SearchCondition searchCondition,
                                        String keyword, int page) throws Exception {
        String postCategoryName = null;
        if (postCategory != null) {
            postCategoryName = postCategory.getCategoryName();
        }
        return mockMvc.perform(get("/api/v1/posts/search")
                        .param("category", postCategoryName)
                        .param("condition", searchCondition.getConditionName())
                        .param("keyword", keyword)
                        .param("page", String.valueOf(page)))
                .andDo(print());
    }
}
