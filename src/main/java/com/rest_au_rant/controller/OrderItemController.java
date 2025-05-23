package com.rest_au_rant.controller;

import com.rest_au_rant.annotation.CheckOrderPermission;
import com.rest_au_rant.annotation.CheckRestaurantPermission;
import com.rest_au_rant.model.IdType;
import com.rest_au_rant.model.OrderItem;
import com.rest_au_rant.model.OrderStatus;
import com.rest_au_rant.service.OrderItemService;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@ToString
@RestController
@RequestMapping("/api/orderItems")
public class OrderItemController {

    private final OrderItemService orderItemService;

    @Autowired
    public OrderItemController( OrderItemService orderItemService) {

        this.orderItemService = orderItemService;
    }

    // Add a dish to an order
    @CheckOrderPermission
    @PostMapping("/{orderId}/{dishId}")
    public ResponseEntity<OrderItem> addOrderItem(
            @PathVariable Long orderId, @PathVariable Long dishId) {

        OrderItem savedItem = orderItemService.addOrderItem(orderId,dishId);
        return ResponseEntity.ok(savedItem);
    }

    // Get all order items
    @GetMapping
    public List<OrderItem> getAllOrderItems() {
        return orderItemService.getAllOrderItems();
    }

    //  Get all items for a specific order
    @CheckOrderPermission
    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<OrderItem>> getOrderItemsByOrder(@PathVariable Long orderId) {
        List<OrderItem> items = orderItemService.getOrderItemsByOrder(orderId);
        return ResponseEntity.ok(items);
    }

    //Get orders for a specific restaurant with a specific status
    @GetMapping("/restaurant")
    public ResponseEntity<List<OrderItem>> getOrdersByRestaurantAndStatus(
            @RequestParam Long restaurantId,
            @RequestParam List<String> status) {

        List<OrderStatus> orderStatuses = status.stream()
                .map(OrderStatus::valueOf) // Convert from String to Enum
                .toList();

        List<OrderItem> orders = orderItemService.getOrdersByRestaurantAndOrderStatus(restaurantId, orderStatuses);
        return ResponseEntity.ok(orders);
    }


    //  Get all items for a specific restaurant
    @GetMapping("/order/restaurant_{restaurantId}")
    public ResponseEntity<List<OrderItem>> getOrderItemsByRestaurant(@PathVariable Long restaurantId) {
        List<OrderItem> items = orderItemService.getOrderItemsByRestaurant(restaurantId);
        return ResponseEntity.ok(items);
    }

    // Update order item status
    @PutMapping("/{orderItemId}/status/{status}")
    public ResponseEntity<OrderItem> updateOrderItemStatus(
            @PathVariable Long orderItemId, @PathVariable OrderStatus status) {
        OrderItem updatedItem = orderItemService.updateOrderItemStatus(orderItemId,status);
        return ResponseEntity.ok(updatedItem);
    }

    // Delete an order item
    @DeleteMapping("/{orderItemId}")
    public ResponseEntity<?> deleteOrderItem(@PathVariable Long orderItemId) {
        orderItemService.deleteOrderItem(orderItemId);
        return (ResponseEntity<?>) ResponseEntity.ok();
    }
}



