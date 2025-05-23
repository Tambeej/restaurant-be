package com.rest_au_rant.repository;

import com.rest_au_rant.model.*;
import com.rest_au_rant.model.user.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByTable(RestaurantTable table);

    @Query("SELECT o.table.restaurant FROM Order o WHERE o.id = :orderID")
    List<Order> findByRestaurant(Restaurant restaurant);




    @Query("SELECT o.table.restaurant FROM Order o WHERE o.id = :orderID")
    Restaurant findRestaurantByOrderId(@Param("orderID") Long orderID);

    @Query("SELECT o.table FROM Order o WHERE o.id = :orderID")
    RestaurantTable findByTableByOrderId(Long orderId);

    @Query("SELECT o.client FROM Order o WHERE o.id = :orderId")
    Client findClientByOrderId(@Param("orderId") Long orderId);
}
