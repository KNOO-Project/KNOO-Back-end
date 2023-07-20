package com.woopaca.knoo.controller;

import com.woopaca.knoo.controller.dto.restaurant.RestaurantListDto;
import com.woopaca.knoo.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping(path = "/api/restaurants")
@RestController
public class RestaurantController {

    private final RestaurantService restaurantService;

    @GetMapping
    public ResponseEntity<List<RestaurantListDto>> allRestaurantList() {
        List<RestaurantListDto> allRestaurants = restaurantService.getAllRestaurants();
        return ResponseEntity.ok().body(allRestaurants);
    }
}
