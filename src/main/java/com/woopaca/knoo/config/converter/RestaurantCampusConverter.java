package com.woopaca.knoo.config.converter;

import com.woopaca.knoo.entity.value.Campus;
import org.springframework.core.convert.converter.Converter;

public class RestaurantCampusConverter implements Converter<String, Campus> {

    @Override
    public Campus convert(String campusName) {
        return Campus.of(campusName);
    }
}
