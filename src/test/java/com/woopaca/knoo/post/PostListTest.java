package com.woopaca.knoo.post;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.woopaca.knoo.entity.EmailVerify;
import com.woopaca.knoo.entity.Post;
import com.woopaca.knoo.entity.PostCategory;
import com.woopaca.knoo.entity.User;
import com.woopaca.knoo.repository.PostRepository;
import com.woopaca.knoo.repository.UserRepository;
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
import java.time.format.DateTimeFormatter;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser
@Transactional
public class PostListTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    UserRepository userRepository;
    @Autowired
    PostRepository postRepository;

    private User user;

    @BeforeEach
    void beforeEach() {
        user = User.builder()
                .username("test")
                .password("password")
                .email("test@test")
                .name("test")
                .emailVerify(EmailVerify.ENABLE)
                .verificationCode("test")
                .joinDate(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm").format(LocalDateTime.now()))
                .build();
        userRepository.save(user);
    }

    @Test
    @DisplayName("게시글 리스트 조회 - 성공")
    void getPostListSuccess() throws Exception {
        //given
        for (int i = 0; i < 30; i++) {
            writeDummyPost();
        }

        //when
        ResultActions resultActions = resultActions("free", 1);

        //then
        resultActions.andExpect(status().isOk());

    }

    @Test
    @DisplayName("게시글 리스트 조회 실패 - 페이지 수 초과")
    void getPostListFailOutOfPages() throws Exception {
        //given
        for (int i = 0; i < 30; i++) {
            writeDummyPost();
        }

        //when
        ResultActions resultActions = resultActions("free", 3);

        //then
        resultActions.andExpect(status().isBadRequest());

    }

    @Test
    @DisplayName("게시글 리스트 조회 실패 - 유효하지 않은 페이지")
    void getPostListFailInvalidPage() throws Exception {
        //given
        for (int i = 0; i < 30; i++) {
            writeDummyPost();
        }

        //when
        ResultActions resultActions = resultActions("free", -1);

        //then
        resultActions.andExpect(status().isBadRequest());

    }

    @Test
    @DisplayName("게시글 리스트 조회 실패 - 없는 카테고리")
    void getPostListFailNonexistentCategory() throws Exception {
        //given

        //when
        ResultActions resultActions = resultActions("test", 1);

        //then
        resultActions.andExpect(status().isBadRequest());

    }

    private void writeDummyPost() {
        Post post = Post.builder()
                .postTitle("test")
                .postContent("test")
                .postCategory(PostCategory.FREE)
                .postDate(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm").format(LocalDateTime.now()))
                .isAnonymous(true)
                .build();
        post.writtenBy(user);
        postRepository.save(post);
    }

    private ResultActions resultActions(String postCategory, int page) throws Exception {
        return mockMvc.perform(get("/api/v1/posts/" + postCategory)
                        .param("page", String.valueOf(page)))
                .andDo(print());
    }
}
