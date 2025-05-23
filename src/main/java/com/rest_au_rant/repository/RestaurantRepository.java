package com.rest_au_rant.repository;

import com.rest_au_rant.model.user.Kitchen;
import com.rest_au_rant.model.user.Manager;
import com.rest_au_rant.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    Optional<Restaurant> findByManager(Manager manager);
    Optional<Restaurant> findByKitchen(Kitchen kitchen);

    @Query("SELECT restaurant.id FROM RestaurantTable WHERE id = :tableId")
    Optional<Long> findRestaurantByTableId(Long tableId);

    @Query("SELECT kitchen FROM Restaurant WHERE id = :kitchenId")
    Optional<Restaurant> findRestaurantByKitchenId(Long kitchenId);
}
