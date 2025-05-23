package com.rest_au_rant.service;

import com.rest_au_rant.exception.ResourceNotFoundException;
import com.rest_au_rant.exception.RestaurantException;
import com.rest_au_rant.model.*;
import com.rest_au_rant.repository.DishRepository;
import com.rest_au_rant.repository.OrderItemRepository;
import com.rest_au_rant.repository.OrderRepository;
import com.rest_au_rant.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderItemService {
    private final OrderItemRepository orderItemRepository;
    private final OrderRepository orderRepository;
    private final DishRepository dishRepository;
    private final RestaurantRepository restaurantRepository;

    @Autowired
    public OrderItemService(OrderItemRepository orderItemRepository, OrderRepository orderRepository, DishRepository dishRepository, RestaurantRepository restaurantRepository) {
        this.orderItemRepository = orderItemRepository;
        this.orderRepository = orderRepository;
        this.dishRepository = dishRepository;
        this.restaurantRepository = restaurantRepository;
    }

    public OrderItem addOrderItem(
            Long orderId, Long dishId) {
        Restaurant dishRestaurant = dishRepository.findRestaurantByDishId(dishId);
        Restaurant orderRestaurant = orderRepository.findRestaurantByOrderId(orderId);
        if (!orderRestaurant.equals(dishRestaurant)) {
            throw new RestaurantException("Dish with ID " + dishId + " does not belong the restaurant.");
        }
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Error: order with ID " + orderId + " not found."));
        Dish dish = dishRepository.findById(dishId)
                .orElseThrow(() -> new ResourceNotFoundException("Error: dish with ID " + dishId + " not found."));

        OrderItem orderItem = new OrderItem();
        orderItem.setOrder(order);
        orderItem.setDish(dish);
        orderItem.setStatus(OrderStatus.ORDERED);

        return orderItemRepository.save(orderItem);
    }

    public List<OrderItem> getAllOrderItems() {
        return orderItemRepository.findAll();
    }

    public List<OrderItem> getOrderItemsByOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Error: order with ID " + orderId + " not found."));

        return orderItemRepository.findByOrder(order);
    }

    public OrderItem updateOrderItemStatus(
            Long orderItemId, OrderStatus status) {
        OrderItem existingItem = orderItemRepository.findById(orderItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Error: order with ID " + orderItemId + " not found."));

        existingItem.setStatus(status);
        return orderItemRepository.save(existingItem);
    }

    public void deleteOrderItem(Long orderItemId) {
        if (!orderItemRepository.existsById(orderItemId)) {
            throw new ResourceNotFoundException("Error: Item in Order " + orderItemId + " does not exists.");
        }
        orderItemRepository.deleteById(orderItemId);
    }

    public List<OrderItem> getOrderItemsByRestaurant(Long restaurantId) {
        return orderItemRepository.findByRestaurant(restaurantId);
    }

    public List<OrderItem> getOrdersByRestaurantAndOrderStatus(Long restaurantId, List<OrderStatus> status) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Error: restaurant with ID " + restaurantId + " not found."));
        return orderItemRepository.findActiveItemsByRestaurant(restaurant, status);
    }
}
