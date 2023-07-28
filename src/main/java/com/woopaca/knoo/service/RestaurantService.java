package com.woopaca.knoo.service;

import com.woopaca.knoo.controller.dto.restaurant.RestaurantListResponseDto;
import com.woopaca.knoo.entity.value.Campus;

import java.util.List;

public interface RestaurantService {

    List<RestaurantListResponseDto> getAllRestaurants();

    List<RestaurantListResponseDto> getRestaurantsByCampus(final Campus campus);
}
