package com.woopaca.knoo.controller.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.woopaca.knoo.controller.auth.dto.SignInRequestDto;
import com.woopaca.knoo.repository.UserRepository;
import com.woopaca.knoo.service.AuthService;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class UserControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper mapper;
    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    AuthService authService;

    @Test
    @DisplayName("회원 프로필 조회")
    void userProfileTest() throws Exception {
        //given
        String token = authService.signIn(SignInRequestDto.builder()
                .username("jcw1031")
                .password("Chan!1057")
                .build());

        //when
        ResultActions resultActions = resultActions(token);

        //then
        resultActions.andExpect(status().isOk());

    }

    private ResultActions resultActions(String token) throws Exception {
        return mockMvc.perform(get("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", token))
                .andDo(print());
    }
}