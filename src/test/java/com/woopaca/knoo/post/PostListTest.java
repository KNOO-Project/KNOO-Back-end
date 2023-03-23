package com.woopaca.knoo.post;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

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

    @Test
    @DisplayName("게시글 리스트 조회 - 성공")
    void getPostListSuccess() throws Exception {
        //given

        //when
        ResultActions resultActions = resultActions("free");

        //then
        resultActions.andExpect(status().isOk());

    }

    @Test
    @DisplayName("게시글 리스트 조회 실패 - 없는 카테고리")
    void getPostListFail() throws Exception {
        //given
        String category = "test";

        //when
        ResultActions resultActions = resultActions(category);

        //then
        resultActions.andExpect(status().isNotFound());

    }

    private ResultActions resultActions(String postCategory) throws Exception {
        return mockMvc.perform(get("/api/v1/posts/" + postCategory))
                .andDo(print());
    }
}
