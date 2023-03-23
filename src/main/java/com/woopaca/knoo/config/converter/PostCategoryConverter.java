package com.woopaca.knoo.config.converter;

import com.woopaca.knoo.entity.PostCategory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class PostCategoryConverter implements Converter<String, PostCategory> {

    @Override
    public PostCategory convert(String categoryName) {
        return PostCategory.hasCategoryName(categoryName);
    }
}
