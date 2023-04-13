package com.woopaca.knoo.comment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.woopaca.knoo.config.jwt.JwtProvider;
import com.woopaca.knoo.controller.dto.auth.SignInUser;
import com.woopaca.knoo.controller.dto.comment.WriteCommentRequestDto;
import com.woopaca.knoo.controller.dto.post.WritePostRequestDto;
import com.woopaca.knoo.entity.EmailVerify;
import com.woopaca.knoo.entity.PostCategory;
import com.woopaca.knoo.entity.User;
import com.woopaca.knoo.repository.UserRepository;
import com.woopaca.knoo.service.CommentService;
import com.woopaca.knoo.service.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@WithMockUser
public class WriteReplyTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper mapper;
    @Autowired
    UserRepository userRepository;
    @Autowired
    PostService postService;
    @Autowired
    CommentService commentService;
    @Autowired
    JwtProvider jwtProvider;

    private String authorization;
    private Long commentId;

    @BeforeEach
    void beforeEach() {
        User user = User.builder()
                .username("test")
                .password("password")
                .email("test@test")
                .name("test")
                .emailVerify(EmailVerify.ENABLE)
                .verificationCode("test")
                .joinDate(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm").format(LocalDateTime.now()))
                .build();
        userRepository.save(user);

        authorization = "Bearer " + jwtProvider.createToken(user, 10);

        WritePostRequestDto writePostRequestDto = WritePostRequestDto.builder()
                .postTitle("test")
                .postContent("test")
                .postCategory(PostCategory.FREE)
                .isAnonymous(false)
                .build();

        SignInUser signInUser = SignInUser.builder()
                .id(user.getId())
                .username(user.getUsername())
                .build();
        Long postId = postService.writePost(signInUser, writePostRequestDto);

        WriteCommentRequestDto writeCommentRequestDto =
                new WriteCommentRequestDto("test comment");
        commentId =
                commentService.writeComment(signInUser, writeCommentRequestDto, postId, null);
    }

    @Test
    @DisplayName("대댓글 작성 성공")
    void writeReplySuccess() throws Exception {
        //given
        WriteCommentRequestDto writeCommentRequestDto = new WriteCommentRequestDto("test reply");

        //when
        ResultActions resultActions = resultActions(writeCommentRequestDto);

        //then
        resultActions.andExpect(status().isCreated())
                .andExpect(content().string("대댓글 작성이 완료되었습니다."));

    }

    private ResultActions resultActions(WriteCommentRequestDto writeCommentRequestDto) throws Exception {
        return mockMvc.perform(post("/api/v1/comments/reply")
                        .param("comment_id", commentId.toString())
                        .header(HttpHeaders.AUTHORIZATION, authorization)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(writeCommentRequestDto)))
                .andDo(print());
    }
}
