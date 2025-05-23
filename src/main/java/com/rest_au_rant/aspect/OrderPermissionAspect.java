package com.rest_au_rant.aspect;

import com.rest_au_rant.exception.ResourceNotFoundException;
import com.rest_au_rant.exception.UnauthorizedException;
import com.rest_au_rant.model.*;
import com.rest_au_rant.model.user.Client;
import com.rest_au_rant.model.user.User;
import com.rest_au_rant.repository.*;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class OrderPermissionAspect {

    private final UserRepository userRepository;
    private final WaiterAssignmentRepository waiterAssignmentRepository;
    private final OrderRepository orderRepository;

    public OrderPermissionAspect(UserRepository userRepository,
                                  WaiterAssignmentRepository waiterAssignmentRepository,
                                 OrderRepository orderRepository) {
        this.userRepository = userRepository;
        this.waiterAssignmentRepository = waiterAssignmentRepository;
        this.orderRepository = orderRepository;
    }

    @Before("@annotation(com.rest_au_rant.annotation.CheckOrderPermission)")
    public void checkPermission(JoinPoint joinPoint) {

        Object[] args = joinPoint.getArgs();
        if (args.length == 0 || !(args[0] instanceof Long orderId)) {
            throw new IllegalArgumentException("Expected a Long ID as the first argument");
        }
        //Receive the order
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        //Receive the restaurant
        Restaurant restaurant = order.getTable().getRestaurant();
        if (restaurant == null) {
            throw new ResourceNotFoundException("No restaurant found for order ID: " + orderId);
        }
        //Receive the table
        RestaurantTable table = order.getTable();

        //TODO add when adding assignment logic
        //Waiter waiter = waiterAssignmentRepository.findByTable(table).getWaiter();

        Client client = orderRepository.findClientByOrderId(orderId);


        // Extract user from JWT token
        JwtAuthenticationToken jwt = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        String userEmail = jwt.getTokenAttributes().get("email").toString();

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        //You can't update an order if you're not the manager of the restaurant, the waiter of the table or the client of the table
//        if (restaurant.getManager().getId() != (user.getId()) && user.getId() != waiter.getId() && user.getId() != client.getId()) {
        if ((restaurant.getManager().getId() != user.getId())&& (user.getId() != client.getId())&& (user.getId()!=restaurant.getKitchen().getId())) {
            throw new UnauthorizedException("You are not authorized to modify this restaurant.");
        }
    }
}
