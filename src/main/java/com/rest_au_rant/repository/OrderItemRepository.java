package com.rest_au_rant.repository;

import com.rest_au_rant.model.Order;
import com.rest_au_rant.model.OrderItem;
import com.rest_au_rant.model.OrderStatus;
import com.rest_au_rant.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    List<OrderItem> findByOrder(Order order);

    @Query("SELECT oi FROM OrderItem oi WHERE oi.order.table.restaurant.id = :restaurantId")
    List<OrderItem> findByRestaurant(Long restaurantId);

    @Query("SELECT oi FROM OrderItem oi " +
            "WHERE oi.order.table.restaurant = :restaurant " +
            "AND oi.status IN (:statuses)")
    List<OrderItem> findActiveItemsByRestaurant(@Param("restaurant") Restaurant restaurant,
                                                @Param("statuses") List<OrderStatus> statuses);

}
