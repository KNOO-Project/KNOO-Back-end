package com.woopaca.knoo.config.converter;

import com.woopaca.knoo.entity.attr.PostCategory;
import org.springframework.core.convert.converter.Converter;

public class PostCategoryConverter implements Converter<String, PostCategory> {

    @Override
    public PostCategory convert(String categoryName) {
        return PostCategory.hasCategoryName(categoryName);
    }
}
