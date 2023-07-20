package com.woopaca.knoo.service;

import com.woopaca.knoo.controller.dto.restaurant.RestaurantListDto;

import java.util.List;

public interface RestaurantService {

    List<RestaurantListDto> getAllRestaurants();
}
