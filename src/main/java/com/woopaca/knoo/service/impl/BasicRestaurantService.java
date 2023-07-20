package com.woopaca.knoo.service.impl;

import com.woopaca.knoo.controller.dto.restaurant.RestaurantListDto;
import com.woopaca.knoo.entity.Restaurant;
import com.woopaca.knoo.repository.RestaurantRepository;
import com.woopaca.knoo.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class BasicRestaurantService implements RestaurantService {

    private final RestaurantRepository restaurantRepository;

    @Override
    public List<RestaurantListDto> getAllRestaurants() {
        List<Restaurant> restaurants = restaurantRepository.findAll();
        return restaurants.stream()
                .map(RestaurantListDto::from)
                .collect(Collectors.toList());
    }
}