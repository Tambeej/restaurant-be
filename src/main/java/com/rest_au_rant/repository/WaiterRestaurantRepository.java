package com.rest_au_rant.repository;


import com.rest_au_rant.model.user.Waiter;
import com.rest_au_rant.model.WaiterRestaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WaiterRestaurantRepository extends JpaRepository<WaiterRestaurant, Long> {

    WaiterRestaurant findByWaiterId(Long waiterId);
    Optional<WaiterRestaurant> findByWaiterIdAndRestaurantId(Long waiterId, Long restaurantId);

    @Query("SELECT wr.waiter FROM WaiterRestaurant wr WHERE wr.restaurant.id = :restaurantId")
    List<Waiter> findWaitersByRestaurantId(@Param("restaurantId") Long restaurantId);
}
