package com.rest_au_rant.controller;

import com.rest_au_rant.annotation.CheckRestaurantPermission;
import com.rest_au_rant.model.*;
import com.rest_au_rant.model.user.Waiter;
import com.rest_au_rant.service.WaiterRestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/waiter-restaurant")
@RequiredArgsConstructor
public class WaiterRestaurantController {

    private final WaiterRestaurantService waiterRestaurantService;

    @CheckRestaurantPermission(IdType.RESTAURANT)
    @PostMapping( value = "/assign",consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WaiterRestaurant> assignWaiterToRestaurant(
            @RequestParam("restaurant_id") Long restaurantId, @RequestParam("waiter_id") Long waiterId ) {
        WaiterRestaurant result = waiterRestaurantService.assignWaiterToRestaurant(waiterId, restaurantId);
        return ResponseEntity.ok(result);
    }

    @CheckRestaurantPermission(IdType.RESTAURANT)
    @PostMapping(value = "/assign-by-email", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<WaiterRestaurant> assignWaiterByEmail(
            @RequestParam("email") String email, @RequestParam("restaurant_id") Long restaurantId) {
        WaiterRestaurant result = waiterRestaurantService.assignWaiterToRestaurantByEmail(email, restaurantId);
        return ResponseEntity.ok(result);
    }

    // Get all waiters for a specific restaurant
    @CheckRestaurantPermission(IdType.RESTAURANT)
    @GetMapping(value = "/get_waiters/{restaurant_id}", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<List<Waiter>> getWaitersByRestaurant(@PathVariable("restaurant_id") Long restaurantId) {
        List<Waiter> allWaiterRestaurant = waiterRestaurantService.getWaitersByRestaurant(restaurantId);
        return ResponseEntity.ok(allWaiterRestaurant);
    }

    // Get all waiters assignments
    @GetMapping()
    public ResponseEntity<List<WaiterRestaurant>> getWaiters() {
        List<WaiterRestaurant> allWaiterRestaurant = waiterRestaurantService.getAllWaiters();
        return ResponseEntity.ok(allWaiterRestaurant);
    }

    @CheckRestaurantPermission(IdType.RESTAURANT)
    @PostMapping(value = "/assign",consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<WaiterRestaurant> getAssignment(
            @RequestParam("restaurant_id") Long restaurantId,
            @RequestParam("waiter_id") Long waiterId) {

        WaiterRestaurant assignment = waiterRestaurantService.assignWaiterToRestaurant(waiterId, restaurantId);
        return ResponseEntity.ok(assignment);
    }

    @CheckRestaurantPermission(IdType.RESTAURANT)
    @DeleteMapping(value = "/remove", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<WaiterRestaurant> removeWaiterFromRestaurant(
            @RequestParam("restaurant_id") Long restaurantId, @RequestParam("waiter_id") Long waiterId ) {
        waiterRestaurantService.removeWaiterFromRestaurant(waiterId, restaurantId);
        return  ResponseEntity.noContent().build();
    }
}