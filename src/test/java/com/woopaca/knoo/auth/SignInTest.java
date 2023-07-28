package com.woopaca.knoo.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.woopaca.knoo.controller.dto.auth.SignInRequestDto;
import com.woopaca.knoo.controller.dto.auth.SignUpRequestDto;
import com.woopaca.knoo.entity.User;
import com.woopaca.knoo.repository.UserRepository;
import com.woopaca.knoo.service.AuthService;
import com.woopaca.knoo.service.MailService;
import org.junit.jupiter.api.BeforeEach;
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
public class SignInTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper mapper;
    @Autowired
    UserRepository userRepository;
    @Autowired
    AuthService authService;

    @MockBean
    MailService mailService;

    @BeforeEach
    void beforeEach() {
        doNothing().when(mailService).sendAuthMail(anyString(), anyString());

        SignUpRequestDto signUpRequestDto = SignUpRequestDto.builder()
                .username("username")
                .password("password")
                .passwordCheck("password")
                .name("test")
                .email("test@smail.kongju.ac.kr")
                .build();
        Long userId = authService.signUp(signUpRequestDto);

        User user = userRepository.findById(userId).orElse(null);
        user.verify();
    }

    @Test
    @DisplayName("로그인 성공")
    void signInSuccess() throws Exception {
        //given
        SignInRequestDto signInRequestDto = SignInRequestDto.builder()
                .username("username")
                .password("password")
                .build();

        //when
        ResultActions resultActions = resultActions(signInRequestDto);

        //then
        resultActions.andExpect(status().isOk());

    }

    @Test
    @DisplayName("로그인 실패 - 아이디 틀림")
    void signInFailIncorrectUsername() throws Exception {
        //given
        SignInRequestDto signInRequestDto = SignInRequestDto.builder()
                .username("wrong")
                .password("password")
                .build();

        //when
        ResultActions resultActions = resultActions(signInRequestDto);

        //then
        resultActions.andExpect(status().isUnauthorized());

    }

    @Test
    @DisplayName("로그인 실패 - 비밀번호 틀림")
    void signInFailIncorrectPassword() throws Exception {
        //given
        SignInRequestDto signInRequestDto = SignInRequestDto.builder()
                .username("username")
                .password("wrong")
                .build();

        //when
        ResultActions resultActions = resultActions(signInRequestDto);

        //then
        resultActions.andExpect(status().isUnauthorized());

    }

    private ResultActions resultActions(SignInRequestDto signInRequestDto) throws Exception {
        return mockMvc.perform(post("/api/auth/sign-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(signInRequestDto)))
                .andDo(print());
    }
}
