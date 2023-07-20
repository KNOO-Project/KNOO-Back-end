package com.woopaca.knoo.entity;

import com.woopaca.knoo.entity.value.Coordinate;
import com.woopaca.knoo.entity.value.CuisineType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "restaurant_id")
    private Long id;

    @Column(name = "restaurant_name")
    private String restaurantName;

    @Column(name = "address")
    private String address;

    @Embedded
    private Coordinate coordinate;

    @Enumerated(EnumType.STRING)
    @Column(name = "cuisine_type")
    private CuisineType cuisineType;

    @Builder
    public Restaurant(String restaurantName, String address, Coordinate coordinate, CuisineType cuisineType) {
        this.restaurantName = restaurantName;
        this.address = address;
        this.coordinate = coordinate;
        this.cuisineType = cuisineType;
    }
}