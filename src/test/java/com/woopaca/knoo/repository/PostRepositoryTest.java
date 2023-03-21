package com.woopaca.knoo.repository;

import com.woopaca.knoo.entity.EmailVerify;
import com.woopaca.knoo.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;

@SpringBootTest
class PostRepositoryTest {

    @Autowired
    PostRepository postRepository;

    @Test
    void test() {
        postRepository.findByCommentWriter(User.builder()
                .username("asdf")
                .password("asdf")
                .name("asdf")
                .email("asdf")
                .emailVerify(EmailVerify.ENABLE)
                .verificationCode("asdf")
                .build(), PageRequest.of(0, 5));
    }
}