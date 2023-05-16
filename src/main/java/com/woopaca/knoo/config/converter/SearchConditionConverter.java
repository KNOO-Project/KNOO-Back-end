package com.woopaca.knoo.config.converter;

import com.woopaca.knoo.controller.dto.post.SearchCondition;
import org.springframework.core.convert.converter.Converter;

public class SearchConditionConverter implements Converter<String, SearchCondition> {

    @Override
    public SearchCondition convert(String searchConditionName) {
        return SearchCondition.hasSearchCondition(searchConditionName);
    }
}
