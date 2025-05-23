package com.rest_au_rant.controller;

import com.rest_au_rant.annotation.CheckOrderPermission;
import com.rest_au_rant.annotation.CheckRestaurantPermission;
import com.rest_au_rant.model.IdType;
import com.rest_au_rant.model.Order;
import com.rest_au_rant.model.OrderItem;
import com.rest_au_rant.model.OrderStatus;
import com.rest_au_rant.service.OrderService;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@ToString
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // Get all orders
    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        List<Order> allOrders = orderService.getAllOrders();
        return ResponseEntity.ok(allOrders);
    }

    //  Get all orders for a specific restaurant
    @CheckRestaurantPermission(IdType.RESTAURANT)
    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<List<Order>> getOrdersByRestaurant(@PathVariable Long restaurantId) {
        List<Order> order = orderService.getOrdersByRestaurant(restaurantId);
        return ResponseEntity.ok(order);
    }


    //  Get all orders for a specific table
    @CheckOrderPermission
    @GetMapping("/table/{tableId}")
    public ResponseEntity<List<Order>> getOrdersByTable(@PathVariable Long tableId) {
        List<Order> order = orderService.getOrdersByTable(tableId);
        return ResponseEntity.ok(order);
    }

    //Create a new order
    @PostMapping("/add")
    public ResponseEntity<Order> addOrder(@RequestParam("table_id") Long table_id,
                                          @RequestParam(value = "user_id", required = false) Long user_id) {
        Order newOrder = orderService.createAnOrder(user_id, table_id);
        return ResponseEntity.ok(newOrder);
    }

}
