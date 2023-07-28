package com.woopaca.knoo.exception.restaurant;

import com.woopaca.knoo.exception.KnooException;

public class CampusNameMatchException extends KnooException {

    public CampusNameMatchException() {
        super(RestaurantError.CAMPUS_NAME_MATCH_ERROR);
    }
}
