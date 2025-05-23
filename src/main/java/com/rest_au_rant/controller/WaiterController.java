package com.rest_au_rant.controller;

import com.rest_au_rant.annotation.CheckRestaurantPermission;
import com.rest_au_rant.model.IdType;
import com.rest_au_rant.model.user.Waiter;
import com.rest_au_rant.service.UserService;
import com.rest_au_rant.service.WaiterService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
@RequestMapping("/api/waiters")
public class WaiterController extends UserController{

    private final WaiterService waiterService;

    public WaiterController(UserService userService, WaiterService waiterService) {
        super(userService);
        this.waiterService = waiterService;
    }

    @CheckRestaurantPermission(IdType.RESTAURANT)
    @GetMapping("/{restaurantId}")
    public ResponseEntity<List<Waiter>> getWaitersInRestaurant(@PathVariable Long restaurantId) {
        List<Waiter> waitersInRestaurant = waiterService.getAllWaitersInRestaurant(restaurantId);
        return ResponseEntity.ok(waitersInRestaurant);
    }
}
