package com.rest_au_rant.repository;

import com.rest_au_rant.model.*;
import com.rest_au_rant.model.user.User;
import com.rest_au_rant.model.user.Waiter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WaiterAssignmentRepository extends JpaRepository<WaiterAssignment, Long> {
    List<WaiterAssignment> findByWaiter(User waiter);
    WaiterAssignment findByTable(RestaurantTable table);

    @Query("SELECT wa.table FROM WaiterAssignment wa WHERE wa.waiter = :waiter AND wa.table.restaurant = :restaurant")
    List<WaiterAssignment> findTablesByWaiterAndRestaurant(@Param("waiter") Waiter waiter,
                                                          @Param("restaurant") Restaurant restaurant);
    @Query("SELECT wa.table FROM WaiterAssignment wa WHERE wa.table.restaurant = :restaurant")
    Restaurant findRestaurantByTableId(@Param("tableId") Long tableId);

}