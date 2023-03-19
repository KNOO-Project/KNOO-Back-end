package com.woopaca.knoo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.woopaca.knoo.controller.dto.SignInRequestDto;
import com.woopaca.knoo.entity.EmailVerify;
import com.woopaca.knoo.entity.User;
import com.woopaca.knoo.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
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
public class SignInTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper mapper;
    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("로그인 성공")
    void signInSuccess() throws Exception {
        //given
        User user = User.builder()
                .username("username")
                .password("password")
                .name("test")
                .email("test@smail.kongju.ac.kr")
                .joinDate(new Date().toString())
                .emailVerify(EmailVerify.ENABLE)
                .build();
        userRepository.save(user);

        SignInRequestDto signInRequestDto = SignInRequestDto.builder()
                .username("username")
                .password("password")
                .build();

        //when
        ResultActions resultActions = resultActions(signInRequestDto);

        //then
        resultActions.andExpect(status().isOk());

    }

    private ResultActions resultActions(SignInRequestDto signInRequestDto) throws Exception {
        return mockMvc.perform(post("/api/v1/auth/sign-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(signInRequestDto)))
                .andDo(print());
    }
}
