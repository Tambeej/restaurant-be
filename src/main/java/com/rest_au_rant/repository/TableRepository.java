package com.rest_au_rant.repository;

import com.rest_au_rant.model.Restaurant;
import com.rest_au_rant.model.RestaurantTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TableRepository extends JpaRepository<RestaurantTable, Long> {
    Optional<RestaurantTable> findByTableNumber(int tableNumber);
    List<RestaurantTable> findByRestaurant(Restaurant restaurant);
    boolean existsByRestaurantIdAndTableNumber(Long restaurantId, int tableNumber);
    Optional<RestaurantTable> findByRestaurantIdAndTableNumber(Long restaurantId, Integer maxNumber);
    RestaurantTable getTableById(Long tableId);

    @Query("SELECT t.restaurant FROM RestaurantTable t WHERE t.id = :tableId")
    Restaurant findRestaurantByTableId(@Param("tableId") Long tableId);

    @Query("SELECT MAX(t.tableNumber) FROM RestaurantTable t WHERE t.restaurant.id = :restaurantId")
    Integer findMaxTableNumberByRestaurantId(@Param("restaurantId") Long restaurantId);
}
