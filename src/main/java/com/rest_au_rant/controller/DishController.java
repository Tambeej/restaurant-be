package com.rest_au_rant.controller;

import com.rest_au_rant.annotation.CheckRestaurantPermission;
import com.rest_au_rant.exception.ResourceNotFoundException;
import com.rest_au_rant.model.*;
import com.rest_au_rant.repository.DishRepository;
import com.rest_au_rant.repository.RestaurantRepository;
import com.rest_au_rant.repository.TableRepository;
import com.rest_au_rant.service.DishService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@ToString
@RestController
@RequestMapping("/api/dishes")
public class DishController {

    private final DishService dishService;
    private final RestaurantRepository restaurantRepository;
    private final DishRepository dishRepository;
    private final TableRepository tableRepository;

    @Autowired
    public DishController(DishService dishService, RestaurantRepository restaurantRepository, DishRepository dishRepository, TableRepository tableRepository) {
        this.dishService = dishService;
        this.restaurantRepository = restaurantRepository;
        this.dishRepository = dishRepository;
        this.tableRepository = tableRepository;
    }

    @CheckRestaurantPermission(IdType.RESTAURANT)
    @PostMapping( "/add/{restaurant_id}")
    public ResponseEntity<Dish> addDish(@PathVariable("restaurant_id") Long restaurantId,
                                        @RequestParam("name") String name, @RequestParam("price") double price,
                                        @RequestParam("category") DishCategory category) {
        Dish newDish = dishService.addDish(restaurantId,name,price,category);

        return ResponseEntity.ok(newDish);
    }

    // Get all dishes
    @GetMapping
    public ResponseEntity<List<Dish>> getAllDishes() {
        List<Dish> allDishes = dishService.getAllDishes();
        return ResponseEntity.ok(allDishes);
    }

    // Get all dishes for a specific restaurant
    @GetMapping( "/get_dishes")
    public ResponseEntity<List<Dish>> getDishesByRestaurant(@RequestParam("restaurant_id") Long restaurantId) {
        List<Dish> allDishes = dishService.getDishesByRestaurant(restaurantId);
        return ResponseEntity.ok(allDishes);
    }

    // Get all dishes for a specific restaurant from table_id
    @GetMapping( "/get_dishes_by_table")
    public ResponseEntity<List<Dish>> getDishesByTable(@RequestParam("table_id") Long tableId) {
        RestaurantTable table = tableRepository.findById(tableId)
                .orElseThrow(() -> new ResourceNotFoundException("Table not found"));
        Long restaurantId = table.getRestaurant().getId();

        List<Dish> dishes = dishService.getDishesByRestaurant(restaurantId);
        return ResponseEntity.ok(dishes);
    }

    // Update a dish
    @CheckRestaurantPermission(IdType.DISH)
    @PutMapping("/{dishId}")
    public ResponseEntity<Dish> updateDish(@Valid @PathVariable("dishId") Long dish_id,
                                           @RequestParam("name") String name,
                                           @RequestParam("price") double price,
                                           @RequestParam("category") DishCategory category) {
        if (price < 0) {
            throw new IllegalArgumentException("Price must be 0 or greater");
        }
        Dish existingDish = dishService.updateDish(dish_id, name, price, category);
        return ResponseEntity.ok(existingDish);

    }

    // Delete a dish
    @CheckRestaurantPermission(IdType.RESTAURANT)
    @DeleteMapping("/{dishId}")
    public ResponseEntity<?> deleteDish(@PathVariable Long dishId) {
        dishService.deleteDish(dishId);
        return (ResponseEntity<?>) ResponseEntity.ok();
    }
}
