package com.woopaca.knoo.controller.dto.restaurant;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.woopaca.knoo.entity.Restaurant;
import com.woopaca.knoo.entity.value.Coordinate;
import lombok.Getter;

@Getter
public class RestaurantListResponseDto {

    private final long restaurantId;

    private final String restaurantName;

    private final String address;

    private final Coordinate coordinate;

    private final String cuisineType;

    private final String url;

    protected RestaurantListResponseDto(long restaurantId, String restaurantName, String address,
                                        Coordinate coordinate, String cuisineType, String url) {
        this.restaurantId = restaurantId;
        this.restaurantName = restaurantName;
        this.address = address;
        this.coordinate = coordinate;
        this.cuisineType = cuisineType;
        this.url = url;
    }

    public static RestaurantListResponseDto from(final Restaurant restaurant) {
        return new RestaurantListResponseDto(
                restaurant.getId(), restaurant.getRestaurantName(), restaurant.getAddress(),
                restaurant.getCoordinate(), restaurant.getCuisineType().getTypeName(), restaurant.getUrl()
        );
    }
}
