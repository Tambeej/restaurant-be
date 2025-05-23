package com.rest_au_rant.aspect;

import com.rest_au_rant.annotation.CheckRestaurantPermission;
import com.rest_au_rant.exception.ResourceNotFoundException;
import com.rest_au_rant.exception.UnauthorizedException;
import com.rest_au_rant.model.*;
import com.rest_au_rant.model.user.User;
import com.rest_au_rant.repository.*;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class RestaurantPermissionAspect {

    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;
    private final TableRepository tableRepository;
    private final DishRepository dishRepository;
    private final WaiterRestaurantRepository waiterRestaurantRepository;
    private final WaiterAssignmentRepository waiterAssignmentRepository;

    public RestaurantPermissionAspect(RestaurantRepository restaurantRepository,
                                      UserRepository userRepository, TableRepository tableRepository, DishRepository dishRepository,
                                      WaiterRestaurantRepository waiterRestaurantRepository, WaiterAssignmentRepository waiterAssignmentRepository) {
        this.restaurantRepository = restaurantRepository;
        this.userRepository = userRepository;
        this.tableRepository = tableRepository;
        this.dishRepository = dishRepository;
        this.waiterRestaurantRepository = waiterRestaurantRepository;
        this.waiterAssignmentRepository = waiterAssignmentRepository;
    }

    @Before("@annotation(permission)")
    public void checkPermission(JoinPoint joinPoint, CheckRestaurantPermission permission) {

        Object[] args = joinPoint.getArgs();
        if (args.length == 0 || !(args[0] instanceof Long id)) {
            throw new IllegalArgumentException("Expected a Long ID as the first argument");
        }

        Long restaurantId;

        if (permission.value() == IdType.RESTAURANT) {
            restaurantId = id;
        } else if (permission.value() == IdType.TABLE) {
            Restaurant restaurant = tableRepository.findRestaurantByTableId(id);
            if (restaurant == null) {
                throw new ResourceNotFoundException("No restaurant found for table ID: " + id);
            }
            restaurantId = restaurant.getId();
        }else if (permission.value() == IdType.DISH) {
            Restaurant restaurant = dishRepository.findRestaurantByDishId(id);
            if (restaurant == null) {
                throw new ResourceNotFoundException("No restaurant found for dish ID: " + id);
            }
            restaurantId = restaurant.getId();
        }else if (permission.value() == IdType.WAITER) {
            Restaurant restaurant = waiterRestaurantRepository.findByWaiterId(id).getRestaurant();
            if (restaurant == null) {
                throw new ResourceNotFoundException("No restaurant found for waiter ID: " + id);
            }
            restaurantId = restaurant.getId();
        }else if (permission.value() == IdType.ASSIGNMENT) {
            Restaurant restaurant = waiterAssignmentRepository.findRestaurantByTableId(id);
            if (restaurant == null) {
                throw new ResourceNotFoundException("No restaurant found for assignment ID: " + id);
            }
            restaurantId = restaurant.getId();
        }else {
            throw new IllegalStateException("Unknown ID type in permission annotation");
        }

        // Get a restaurant from table
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found"));;

        // Extract user from JWT token
        JwtAuthenticationToken jwt = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        String userEmail = jwt.getTokenAttributes().get("email").toString();

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));


        if (restaurant.getManager().getId() != (user.getId())) {
            throw new UnauthorizedException("You are not authorized to modify this restaurant.");
        }
    }
}
