package com.woopaca.knoo.post;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.woopaca.knoo.entity.Post;
import com.woopaca.knoo.entity.User;
import com.woopaca.knoo.entity.value.EmailVerify;
import com.woopaca.knoo.entity.value.PostCategory;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockUser
@Transactional
@AutoConfigureMockMvc
@SpringBootTest
public class PostPreviewTest {

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
                .joinDate(LocalDateTime.now())
                .build();
        userRepository.save(user);

        for (PostCategory postCategory : PostCategory.values()) {
            for (int i = 0; i < 5; i++) {
                writeDummyPost(postCategory);
            }
        }
    }

    @DisplayName("게시글 미리보기 조회 - 성공")
    @Test
    void postPreviewSuccess() throws Exception {
        // given

        // when
        ResultActions resultActions = resultActions();

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(content().json("[{category: 'free', posts: [{},{},{},{},{}]}, {}, {}, {}, {}, {}]"));
    }

    private void writeDummyPost(PostCategory postCategory) {
        Post post = Post.builder()
                .postTitle("test")
                .postContent("test")
                .postCategory(postCategory)
                .postDate(LocalDateTime.now())
                .isAnonymous(true)
                .build();
        post.writtenBy(user);
        postRepository.save(post);
    }

    private ResultActions resultActions() throws Exception {
        return mockMvc.perform(get("/api/v1/posts"))
                .andDo(print());
    }
}
