package com.woopaca.knoo.config.converter;

import com.woopaca.knoo.entity.value.Campus;
import com.woopaca.knoo.exception.restaurant.CampusNameMatchException;
import org.springframework.core.convert.converter.Converter;

public class RestaurantCampusConverter implements Converter<String, Campus> {

    @Override
    public Campus convert(String campusName) {
        Campus campus = Campus.of(campusName);
        if (campus == null) {
            throw new CampusNameMatchException();
        }

        return campus;
    }
}
