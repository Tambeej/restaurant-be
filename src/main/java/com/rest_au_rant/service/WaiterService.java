package com.rest_au_rant.service;

import com.rest_au_rant.model.user.Waiter;
import com.rest_au_rant.repository.WaiterRepository;

import java.util.List;

public class WaiterService{

    private final WaiterRepository waiterRepository;

    public WaiterService(WaiterRepository waiterRepository) {
        this.waiterRepository = waiterRepository;
    }

    public List<Waiter> getAllWaitersInRestaurant(Long restaurantId) {
        return waiterRepository.getWaiterByRestaurantId(restaurantId);
    }
}
