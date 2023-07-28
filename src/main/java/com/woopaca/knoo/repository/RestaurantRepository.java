package com.woopaca.knoo.repository;

import com.woopaca.knoo.entity.Restaurant;
import com.woopaca.knoo.entity.value.Campus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    List<Restaurant> findByCampus(Campus campus);
}
