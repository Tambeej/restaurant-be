package com.rest_au_rant.repository;

import com.rest_au_rant.model.Dish;
import com.rest_au_rant.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DishRepository extends JpaRepository<Dish, Long> {
    List<Dish> findByRestaurant(Restaurant restaurant);
    boolean existsByRestaurantIdAndName(Long restaurantId,String dishName);

    @Query("SELECT d.restaurant FROM Dish d WHERE d.id = :dishId")
    Restaurant findRestaurantByDishId(@Param("dishId") Long dishId);
}
