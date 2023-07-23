package com.woopaca.knoo.controller;

import com.woopaca.knoo.controller.dto.restaurant.RestaurantListResponseDto;
import com.woopaca.knoo.entity.value.Campus;
import com.woopaca.knoo.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping(path = "/api/restaurants")
@RestController
public class RestaurantController {

    private final RestaurantService restaurantService;

    @GetMapping
    public ResponseEntity<List<RestaurantListResponseDto>> restaurantsList(
            @RequestParam(name = "campus", required = false) final String campus
    ) {
        if (campus != null) {
            return restaurantsByCampus(Campus.of(campus));
        }
        return allRestaurants();
    }

    private ResponseEntity<List<RestaurantListResponseDto>> allRestaurants() {
        List<RestaurantListResponseDto> restaurants = restaurantService.getAllRestaurants();
        return ResponseEntity.ok().body(restaurants);
    }

    private ResponseEntity<List<RestaurantListResponseDto>> restaurantsByCampus(final Campus campus) {
        List<RestaurantListResponseDto> restaurants = restaurantService.getRestaurantsByCampus(campus);
        return ResponseEntity.ok().body(restaurants);
    }
}
