package com.woopaca.knoo.config.converter;

import com.woopaca.knoo.controller.dto.post.UserPostsKind;
import org.springframework.core.convert.converter.Converter;

public class UserPostsKindConverter implements Converter<String, UserPostsKind> {

    @Override
    public UserPostsKind convert(String postsKindName) {
        return UserPostsKind.getUserPostsKind(postsKindName);
    }
}
