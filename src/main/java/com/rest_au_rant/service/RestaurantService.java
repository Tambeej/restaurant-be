package com.rest_au_rant.service;

import com.rest_au_rant.exception.ResourceNotFoundException;
import com.rest_au_rant.exception.RestaurantException;
import com.rest_au_rant.model.*;
import com.rest_au_rant.model.user.Kitchen;
import com.rest_au_rant.model.user.Manager;
import com.rest_au_rant.model.user.User;
import com.rest_au_rant.repository.KitchenRepository;
import com.rest_au_rant.repository.ManagerRepository;
import com.rest_au_rant.repository.RestaurantRepository;
import com.rest_au_rant.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;
    private final ManagerRepository managerRepository;
    private final KitchenRepository kitchenRepository;

    @Autowired
    public RestaurantService(RestaurantRepository restaurantRepository, UserRepository userRepository,
                             ManagerRepository managerRepository,KitchenRepository kitchenRepository) {
        this.restaurantRepository = restaurantRepository;
        this.userRepository = userRepository;
        this.managerRepository = managerRepository;
        this.kitchenRepository = kitchenRepository;
    }

    public Restaurant createRestaurant(String name) {
        // Extract user from JWT token
        JwtAuthenticationToken jwt = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        String userEmail = jwt.getTokenAttributes().get("email").toString();

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userEmail));

        // Upgrade to Manager if not already and role is MANAGER
        Manager manager;
        if (user instanceof Manager) {
            manager = (Manager) user;
        } else if (user.getRole() == Role.MANAGER) {
            // Convert user to Manager by copying over fields and keeping ID
            manager = new Manager(user);
            manager = managerRepository.save(manager); // Now saved as a Manager
        } else {
            throw new RestaurantException("Access Denied: Only managers can create restaurants.");
        }

        Restaurant restaurant = new Restaurant();
        restaurant.setName(name);
        restaurant.setManager(manager); // 💡 Uses Manager subclass

        return restaurantRepository.save(restaurant);
    }

    public List<Restaurant> getAllRestaurants() {
        return restaurantRepository.findAll();
    }

    public Restaurant getRestaurantFromTableId(Long tableId) {
        Long restaurantID = restaurantRepository.findRestaurantByTableId(tableId).orElseThrow(()->new ResourceNotFoundException("There is no restaurant for table id: "+tableId));
        return restaurantRepository.findById(restaurantID).orElseThrow(()->new ResourceNotFoundException("There is no restaurant for table id: "+tableId));
    }

    public Restaurant getRestaurantFromKitchenId(Long kitchenId) {
        Kitchen kitchen = kitchenRepository.findById(kitchenId).orElseThrow(()->new ResourceNotFoundException("There is no kitchen with id: "+kitchenId));
        return restaurantRepository.findByKitchen(kitchen).orElseThrow(()->new ResourceNotFoundException("There is no restaurant for kitchen id: "+kitchenId));

    }

    public Restaurant getRestaurantByManagerEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Manager manager = managerRepository.findById(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Manager not found"));
        return restaurantRepository.findByManager(manager)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found"));
    }
}