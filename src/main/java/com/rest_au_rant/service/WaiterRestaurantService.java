package com.rest_au_rant.service;

import com.rest_au_rant.exception.ResourceNotFoundException;
import com.rest_au_rant.model.*;
import com.rest_au_rant.model.user.User;
import com.rest_au_rant.model.user.Waiter;
import com.rest_au_rant.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class WaiterRestaurantService {

    private final WaiterRestaurantRepository waiterRestaurantRepository;
    private final WaiterRepository waiterRepository;
    private final RestaurantRepository restaurantRepository;
    private final WaiterAssignmentRepository waiterAssignmentRepository;
    private final UserRepository userRepository;

    @Transactional
    public WaiterRestaurant assignWaiterToRestaurant(Long waiterId, Long restaurantId) {
        // Get waiter
        Waiter waiter = waiterRepository.findById(waiterId).orElse(null);
        if (waiter == null) {
            User user = userRepository.findById(waiterId)
                    .orElseThrow(() -> new ResourceNotFoundException("Waiter not found"));
            waiter = new Waiter(user.getId(), user.getUserName(), user.getEmail(), user.getRole());
        }
        //Get Restaurant
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found"));
        //Check if waiter is already assigned to that restaurant
        WaiterRestaurant assignedRestaurant = waiterRestaurantRepository.findByWaiterId(waiterId);
        if (assignedRestaurant!= null && assignedRestaurant.getRestaurant().equals(restaurant)) {
            throw new ResourceNotFoundException("Restaurant assigned to waiter");
        }
        //Build the assignment
        WaiterRestaurant mapping = WaiterRestaurant.builder()
                .waiter(waiter)
                .restaurant(restaurant)
                .build();

        return waiterRestaurantRepository.save(mapping);
    }

    public List<Waiter> getWaitersByRestaurant(Long restaurantId) {
        return waiterRestaurantRepository.findWaitersByRestaurantId(restaurantId);
    }


    public WaiterRestaurant getAssignment(Long waiterId, Long restaurantId) {
        return waiterRestaurantRepository.findByWaiterIdAndRestaurantId(waiterId, restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Assignment not found"));
    }

    public void removeWaiterFromRestaurant(Long waiterId, Long restaurantId) {
        //Check if waiter is in db
        Waiter waiter = waiterRepository.findById(waiterId)
                .orElseThrow(() -> new ResourceNotFoundException("Waiter not found"));
        //Check if restaurant in db
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found"));
        //Check if waiter is working in a restaurant
        WaiterRestaurant assignment = waiterRestaurantRepository.findByWaiterIdAndRestaurantId(waiterId, restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Waiter is not assigned to restaurant."));
        //find all connection of waiters and tables in that restaurant
        List<WaiterAssignment> waitersTables = waiterAssignmentRepository.findTablesByWaiterAndRestaurant(waiter,restaurant);
        waiterAssignmentRepository.deleteAll(waitersTables);
        //Delete the connection between waiter and restaurant
        waiterRestaurantRepository.delete(assignment);

    }

    public List<WaiterRestaurant> getAllWaiters() {
        return waiterRestaurantRepository.findAll();
    }
}
