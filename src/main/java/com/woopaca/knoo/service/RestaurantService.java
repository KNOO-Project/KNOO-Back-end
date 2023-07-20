package com.woopaca.knoo.service;

import com.woopaca.knoo.controller.dto.restaurant.RestaurantListResponseDto;

import java.util.List;

public interface RestaurantService {

    List<RestaurantListResponseDto> getAllRestaurants();
}
