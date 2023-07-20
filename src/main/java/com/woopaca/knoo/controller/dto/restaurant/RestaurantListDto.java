package com.woopaca.knoo.controller.dto.restaurant;

import com.woopaca.knoo.entity.Restaurant;
import com.woopaca.knoo.entity.value.Coordinate;
import lombok.Getter;

@Getter
public class RestaurantListDto {

    private final long restaurantId;
    private final String restaurantName;
    private final String address;
    private final Coordinate coordinate;
    private final String cuisineType;

    protected RestaurantListDto(long restaurantId, String restaurantName, String address,
                                Coordinate coordinate, String cuisineType) {
        this.restaurantId = restaurantId;
        this.restaurantName = restaurantName;
        this.address = address;
        this.coordinate = coordinate;
        this.cuisineType = cuisineType;
    }

    public static RestaurantListDto from(final Restaurant restaurant) {
        return new RestaurantListDto(
                restaurant.getId(), restaurant.getRestaurantName(), restaurant.getAddress(),
                restaurant.getCoordinate(), restaurant.getCuisineType().getTypeName()
        );
    }
}
