package com.woopaca.knoo.post;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.woopaca.knoo.config.jwt.JwtProvider;
import com.woopaca.knoo.controller.post.dto.WritePostRequestDto;
import com.woopaca.knoo.entity.EmailVerify;
import com.woopaca.knoo.entity.PostCategory;
import com.woopaca.knoo.entity.User;
import com.woopaca.knoo.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class PostWriteTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper mapper;
    @Autowired
    UserRepository userRepository;
    @Autowired
    JwtProvider jwtProvider;

    private String authorization;

    @BeforeEach
    void beforeEach() {
        User user = User.builder()
                .username("test")
                .password("test")
                .name("test")
                .email("test")
                .verificationCode("test")
                .emailVerify(EmailVerify.ENABLE)
                .joinDate(String.valueOf(new Date()))
                .build();
        userRepository.save(user);

        authorization = "Bearer " + jwtProvider.createToken(user, 1);
    }

    @Test
    @DisplayName("게시글 작성 - 성공")
    @WithMockUser
    void writePostSuccess() throws Exception {
        //given
        WritePostRequestDto writePostRequestDto = WritePostRequestDto.builder()
                .postTitle("Test Post")
                .postContent("Test post contents")
                .postCategory(PostCategory.FREE)
                .build();

        //when
        ResultActions resultActions = resultActions(writePostRequestDto);

        //then
        resultActions.andExpect(status().isCreated());

    }

    private ResultActions resultActions(WritePostRequestDto writePostRequestDto) throws Exception {
        return mockMvc.perform(post("/api/v1/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", authorization)
                        .content(mapper.writeValueAsString(writePostRequestDto)))
                .andDo(print());
    }
}
