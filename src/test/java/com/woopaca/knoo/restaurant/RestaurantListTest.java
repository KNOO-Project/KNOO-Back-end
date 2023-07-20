package com.woopaca.knoo.restaurant;

import com.woopaca.knoo.entity.Restaurant;
import com.woopaca.knoo.entity.value.Campus;
import com.woopaca.knoo.entity.value.Coordinate;
import com.woopaca.knoo.entity.value.CuisineType;
import com.woopaca.knoo.repository.RestaurantRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockUser
@Transactional
@AutoConfigureMockMvc
@SpringBootTest
public class RestaurantListTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    RestaurantRepository restaurantRepository;

    @DisplayName("모든 맛집 리스트 조회 - 성공")
    @Test
    void getAllRestaurantsListSuccess() throws Exception {
        // given
        restaurantRepository.saveAll(Arrays.asList(
                        new Restaurant("test1", "test1", new Coordinate("37.111", "128.111"), CuisineType.CAFE, Campus.CHEONAN),
                        new Restaurant("test2", "test2", new Coordinate("37.111", "128.111"), CuisineType.KOREAN, Campus.CHEONAN)
                )
        );

        // when
        ResultActions resultActions = mockMvc.perform(get("/api/restaurants"))
                .andDo(print());

        // then
        resultActions.andExpectAll(
                result -> status().isOk(),
                result -> jsonPath("$[0].restaurant_id", 1),
                result -> jsonPath("$[0].restaurant_name", "test1"),
                result -> jsonPath("$[0].cuisine_type", CuisineType.CAFE.getTypeName()),
                result -> content().json("[{}, {}]")
        );
    }
}
