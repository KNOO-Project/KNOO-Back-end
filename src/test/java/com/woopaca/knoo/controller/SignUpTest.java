package com.woopaca.knoo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.woopaca.knoo.controller.dto.SignUpRequestDto;
import com.woopaca.knoo.entity.User;
import com.woopaca.knoo.entity.Verification;
import com.woopaca.knoo.repository.UserRepository;
import com.woopaca.knoo.service.MailService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class SignUpTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper mapper;
    @Autowired
    UserRepository userRepository;

    @MockBean
    MailService mailService;

    @Test
    @DisplayName("회원가입 성공")
    void signUpSuccess() throws Exception {
        //given
        SignUpRequestDto signUpRequestDto = SignUpRequestDto.builder()
                .username("username")
                .password("password")
                .passwordCheck("password")
                .name("test")
                .email("test@smail.kongju.ac.kr")
                .build();

        //when
        ResultActions resultActions = resultActions(signUpRequestDto);

        //then
        resultActions.andExpect(status().isCreated());
    }

    @Test
    @DisplayName("회원가입 실패 - field validation error")
    void signUpFailFieldError() throws Exception {
        //given
        SignUpRequestDto signUpRequestDto = SignUpRequestDto.builder()
                .username("username")
                .password("test")
                .passwordCheck("test")
                .name("test")
                .email("test@smail.kongju.ac.kr")
                .build();

        //when
        ResultActions resultActions = resultActions(signUpRequestDto);

        //then
        resultActions.andExpect(status().isBadRequest());

    }

    @Test
    @DisplayName("회원가입 실패 - 아이디 중복")
    void signUpFailDuplicateUsername() throws Exception {
        //given
        SignUpRequestDto signUpRequestDto1 = SignUpRequestDto.builder()
                .username("username")
                .password("password")
                .passwordCheck("password")
                .name("test")
                .email("test@smail.kongju.ac.kr")
                .build();
        User user = User.join(signUpRequestDto1);
        Verification verification = Verification.createVerification();
        user.setVerification(verification);
        userRepository.save(user);

        SignUpRequestDto signUpRequestDto2 = SignUpRequestDto.builder()
                .username("username")
                .password("password")
                .passwordCheck("password")
                .name("test2")
                .email("test2@smail.kongju.ac.kr")
                .build();

        //when
        ResultActions resultActions = resultActions(signUpRequestDto2);

        //then
        resultActions.andExpect(status().isConflict());

    }

    private ResultActions resultActions(SignUpRequestDto signUpRequestDto) throws Exception {
        doNothing().when(mailService).sendAuthMail(anyString(), anyString());

        return mockMvc.perform(post("/api/v1/auth/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(signUpRequestDto)))
                .andDo(print());
    }
}