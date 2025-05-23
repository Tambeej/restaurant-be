package com.rest_au_rant.controller;

import com.rest_au_rant.annotation.CheckRestaurantPermission;
import com.rest_au_rant.model.IdType;
import com.rest_au_rant.model.Restaurant;
import com.rest_au_rant.service.RestaurantService;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@ToString
@RestController
@RequestMapping("/api/restaurants")
public class RestaurantController {

    private final RestaurantService restaurantService;

    @Autowired
    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @PostMapping(value = "/add", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Restaurant> addRestaurant(@RequestParam("name") String name) {
        Restaurant restaurant = restaurantService.createRestaurant(name);
        return ResponseEntity.ok(restaurant);
    }

    @GetMapping
    public ResponseEntity<List<Restaurant>> getAllRestaurants() {
        List<Restaurant> restaurants = restaurantService.getAllRestaurants();
        return ResponseEntity.ok(restaurants);
    }

    @GetMapping( "/get_restaurant_from_table")
    public ResponseEntity<Restaurant> getRestaurantFromTableId(@RequestParam("table_id") Long table_id) {
        Restaurant restaurant = restaurantService.getRestaurantFromTableId(table_id);
        return ResponseEntity.ok(restaurant);
    }

    @GetMapping( "/get_restaurant_from_kitchen")
    public ResponseEntity<Restaurant> getRestaurantFromKitchenId(@RequestParam("kitchen_id") Long kitchen_id) {
        Restaurant restaurant = restaurantService.getRestaurantFromKitchenId(kitchen_id);
        return ResponseEntity.ok(restaurant);
    }
}
