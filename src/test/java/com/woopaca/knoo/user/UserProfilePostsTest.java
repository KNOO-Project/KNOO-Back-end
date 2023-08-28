package com.woopaca.knoo.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.woopaca.knoo.config.jwt.JwtProvider;
import com.woopaca.knoo.controller.dto.auth.SignInUser;
import com.woopaca.knoo.controller.dto.comment.WriteCommentRequestDto;
import com.woopaca.knoo.controller.dto.post.UserPostsKind;
import com.woopaca.knoo.entity.Post;
import com.woopaca.knoo.entity.User;
import com.woopaca.knoo.entity.value.EmailVerify;
import com.woopaca.knoo.entity.value.PostCategory;
import com.woopaca.knoo.repository.PostRepository;
import com.woopaca.knoo.repository.UserRepository;
import com.woopaca.knoo.service.CommentService;
import com.woopaca.knoo.service.PostService;
import com.woopaca.knoo.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@WithMockUser
@AutoConfigureMockMvc
@SpringBootTest
public class UserProfilePostsTest {

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
    CommentService commentService;
    @Autowired
    UserService userService;
    @Autowired
    JwtProvider jwtProvider;

    private Long postAId;
    private Long postBId;
    private String authorizationA;
    private SignInUser signInUser;

    @BeforeEach
    void beforeEach() {
        User userA = createUser("test");
        userRepository.save(userA);
        authorizationA = "Bearer " + jwtProvider.createToken(userA, 10);
        signInUser = new SignInUser(userA.getId(), userA.getUsername());

        Post postA = createPost("A");
        postA.writtenBy(userA);
        postRepository.save(postA);
        postAId = postA.getId();

        Post postB = createPost("B");
        postB.writtenBy(userA);
        postRepository.save(postB);
        postBId = postB.getId();
    }

    @DisplayName("회원이 작성한 게시글 목록 더보기 - 성공")
    @Test
    void userWritePostListSeeMoreSuccess() throws Exception {
        // given
        WriteCommentRequestDto writeCommentRequestDto = new WriteCommentRequestDto("Test comment");
        commentService.writeComment(signInUser, writeCommentRequestDto, postAId, null);
        commentService.writeComment(signInUser, writeCommentRequestDto, postBId, null);
        postService.changePostLike(signInUser, postAId);
        postService.changePostLike(signInUser, postBId);

        // when
        ResultActions resultActions = resultActions(authorizationA, UserPostsKind.WRITE.getKindName(), 1);

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("posts.[0].post_title").value("test B"));
    }

    @DisplayName("회원이 댓글을 작성한 게시글 목록 더보기 - 성공")
    @Test
    void userCommentPostListSeeMoreSuccess() throws Exception {
        // given
        WriteCommentRequestDto writeCommentRequestDto = new WriteCommentRequestDto("Test comment");
        commentService.writeComment(signInUser, writeCommentRequestDto, postAId, null);
        commentService.writeComment(signInUser, writeCommentRequestDto, postBId, null);
        postService.changePostLike(signInUser, postAId);
        postService.changePostLike(signInUser, postBId);

        // when
        ResultActions resultActions = resultActions(authorizationA, UserPostsKind.COMMENT.getKindName(), 1);

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("posts.[0].post_title").value("test B"));
    }

    @DisplayName("회원이 좋아요 누른 게시글 목록 더보기 - 성공")
    @Test
    void userLikePostListSeeMoreSuccess() throws Exception {
        // given
        WriteCommentRequestDto writeCommentRequestDto = new WriteCommentRequestDto("Test comment");
        commentService.writeComment(signInUser, writeCommentRequestDto, postBId, null);
        commentService.writeComment(signInUser, writeCommentRequestDto, postAId, null);
        postService.changePostLike(signInUser, postBId);
        postService.changePostLike(signInUser, postAId);

        // when
        ResultActions resultActions = resultActions(authorizationA, UserPostsKind.LIKE.getKindName(), 1);

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("posts.[0].post_title").value("test A"));
    }

    @DisplayName("회원 프로필 게시글 목록 더보기 실패 - 유효하지 않은 게시글 종류")
    @Test
    void userProfilePostListSeeMoreFailInvalidKind() throws Exception {
        // given

        // when
        ResultActions resultActions = resultActions(authorizationA, "invalid", 1);

        // then
        resultActions.andExpect(status().isBadRequest())
                .andExpect(jsonPath("error_code").value("KN106"));
    }

    @DisplayName("회원 프로필 게시글 목록 더보기 실패 - 유효하지 않은 페이지")
    @Test
    void userProfilePostListSeeMoreFailInvalidPage() throws Exception {
        // given
        WriteCommentRequestDto writeCommentRequestDto = new WriteCommentRequestDto("Test comment");
        commentService.writeComment(signInUser, writeCommentRequestDto, postBId, null);
        commentService.writeComment(signInUser, writeCommentRequestDto, postAId, null);
        postService.changePostLike(signInUser, postBId);
        postService.changePostLike(signInUser, postAId);

        // when
        ResultActions resultActions = resultActions(authorizationA, UserPostsKind.WRITE.getKindName(), -1);

        // then
        resultActions.andExpect(status().isBadRequest())
                .andExpect(jsonPath("message").value("유효하지 않은 페이지"));
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

    private static Post createPost(String signature) {
        return Post.builder()
                .postTitle("test " + signature)
                .postContent("test" + signature)
                .postCategory(PostCategory.FREE)
                .postDate(LocalDateTime.now())
                .build();
    }

    private ResultActions resultActions(String authorization, String kind, int page) throws Exception {
        return mockMvc.perform(get("/api/users/more")
                        .param("kind", kind)
                        .param("page", String.valueOf(page))
                        .header(HttpHeaders.AUTHORIZATION, authorization))
                .andDo(print());
    }
}
