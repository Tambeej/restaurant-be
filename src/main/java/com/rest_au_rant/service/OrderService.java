package com.rest_au_rant.service;

import com.rest_au_rant.exception.ResourceNotFoundException;
import com.rest_au_rant.model.*;
import com.rest_au_rant.model.user.Client;
import com.rest_au_rant.model.user.User;
import com.rest_au_rant.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final RestaurantRepository restaurantRepository;
    private final TableRepository tableRepository;
    private final UserRepository userRepository;
    private final ClientRepository clientRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository, RestaurantRepository restaurantRepository,
                        TableRepository tableRepository, UserRepository userRepository, ClientRepository clientRepository) {
        this.orderRepository = orderRepository;
        this.restaurantRepository = restaurantRepository;
        this.userRepository = userRepository;
        this.tableRepository = tableRepository;
        this.clientRepository = clientRepository;
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public List<Order> getOrdersByRestaurant(Long restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(()->new ResourceNotFoundException("Error: restaurant with ID " + restaurantId + " not found."));

        return orderRepository.findByRestaurant(restaurant);
    }

    public List<Order> getOrdersByTable(Long tableId) {
        RestaurantTable table = tableRepository.findById(tableId)
                .orElseThrow(()->new ResourceNotFoundException("Error: table with ID " + tableId + " not found."));

        return orderRepository.findByTable(table);
    }

    public Order createAnOrder(Long optionalUserId, Long tableId) {
        Order newOrder = new Order();

        // Extract logged-in user's email
        JwtAuthenticationToken jwt = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        String userEmail = jwt.getTokenAttributes().get("email").toString();

        User currentUser = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Logged-in user not found"));

        // Determine who the client is:
        Client client = null;
        if (currentUser.getRole() == Role.WAITER || currentUser.getRole() == Role.MANAGER) {
            // Waiter or manager: allow using provided user_id
            if (optionalUserId != null) {
                client = clientRepository.findById(optionalUserId)
                        .orElseThrow(() -> new ResourceNotFoundException("Client user not found with ID: " + optionalUserId));
            }
        }else {
            // Client: ignore passed user_id
            client = (Client) currentUser;
        }

        newOrder.setClient(client);



        // Set table
        RestaurantTable table = tableRepository.findById(tableId)
                .orElseThrow(() -> new ResourceNotFoundException("Table not found with ID: " + tableId));
        newOrder.setTable(table);

        return orderRepository.save(newOrder);
    }


}
