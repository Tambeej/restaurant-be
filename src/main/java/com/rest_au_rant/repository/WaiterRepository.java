package com.rest_au_rant.repository;


import com.rest_au_rant.model.user.Waiter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WaiterRepository extends JpaRepository<Waiter, Long> {
    @Query("SELECT wr.restaurant.id FROM WaiterRestaurant wr WHERE wr.waiter.id = :waiterId")
    Long findRestaurantIdByWaiterId(@Param("waiterId") Long waiterId);


    @Query("SELECT wr.waiter.id FROM WaiterRestaurant wr WHERE wr.restaurant.id = :restaurantId")
    List<Waiter> getWaiterByRestaurantId(Long restaurantId);
}
