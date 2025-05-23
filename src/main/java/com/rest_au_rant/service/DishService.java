package com.rest_au_rant.service;

import com.rest_au_rant.exception.ResourceNotFoundException;
import com.rest_au_rant.model.Dish;
import com.rest_au_rant.model.DishCategory;
import com.rest_au_rant.model.Restaurant;
import com.rest_au_rant.repository.DishRepository;
import com.rest_au_rant.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DishService {

    private final DishRepository dishRepository;
    private final RestaurantRepository restaurantRepository;

    @Autowired
    public DishService(DishRepository dishRepository, RestaurantRepository restaurantRepository) {
        this.dishRepository = dishRepository;
        this.restaurantRepository = restaurantRepository;
    }

    public Dish addDish(Long restaurantId,String name,double price,DishCategory category) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElseThrow(() -> new ResourceNotFoundException("Restaurant not found"));
        Dish dish = new Dish();
        dish.setName(name);
        dish.setPrice(price);
        dish.setCategory(category);
        dish.setRestaurant(restaurant);
        boolean exists = dishRepository.existsByRestaurantIdAndName(restaurantId, name);
        if (exists) {
            throw new IllegalArgumentException("A dish with name " + name + " already exists in this restaurant.");

        }
        return dishRepository.save(dish);
    }

    public List<Dish> getAllDishes() {
        return dishRepository.findAll();
    }

    public List<Dish> getDishesByRestaurant(Long restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Error: restaurant with ID " + restaurantId + " not found."));

        return dishRepository.findByRestaurant(restaurant);
    }

    public Dish updateDish(Long dishId, String name, double price, DishCategory category){
        Dish existingDish = dishRepository.findById(dishId)
                .orElseThrow(() -> new ResourceNotFoundException("Error: Dish " + dishId + " does not exists."));

        existingDish.setCategory(category);
        existingDish.setName(name);
        existingDish.setPrice(price);
        return dishRepository.save(existingDish);
    }

    public void deleteDish(Long dishId) {
        if (!dishRepository.existsById(dishId)) {
            throw new RuntimeException("Error: Dish " + dishId + " does not exists.");
        }
        dishRepository.deleteById(dishId);
    }
}
