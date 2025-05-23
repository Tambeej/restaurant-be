package com.rest_au_rant.controller;

import com.google.zxing.WriterException;
import com.rest_au_rant.annotation.CheckRestaurantPermission;
import com.rest_au_rant.exception.UnauthorizedException;
import com.rest_au_rant.model.IdType;
import com.rest_au_rant.model.Restaurant;
import com.rest_au_rant.service.RestaurantService;
import com.rest_au_rant.service.TableService;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import com.rest_au_rant.model.RestaurantTable;

import java.io.IOException;
import java.util.List;

@ToString
@RestController
@RequestMapping("/api/tables")
public class TableController {

    private final TableService tableService;
    private final RestaurantService restaurantService;


    @Autowired
    public TableController(TableService tableService, RestaurantService restaurantService) {
        this.tableService = tableService;
        this.restaurantService = restaurantService;
    }

    //Add a new table to a restaurant
    @CheckRestaurantPermission(IdType.RESTAURANT)
    @PostMapping( "/add")
    public ResponseEntity<RestaurantTable> addTable(@RequestParam("restaurant_id") Long restaurantId) throws UnauthorizedException, IOException, WriterException {

        RestaurantTable savedTable = tableService.addTable(restaurantId);
        Long tableId = savedTable.getId();
        tableService.setQRCode(tableId);
        return ResponseEntity.ok(savedTable);
    }

    //Get all tables
    @GetMapping
    public List<RestaurantTable> getAllTables() {
        return tableService.getAllTables();
    }

    //Get all tables for a specific restaurant
    @CheckRestaurantPermission(IdType.RESTAURANT)
    @GetMapping("/get_tables/restaurant_{restaurantId}")
    public ResponseEntity<List<RestaurantTable>> getTablesByRestaurant(@PathVariable Long restaurantId) {
        List<RestaurantTable> tables = tableService.getTablesByRestaurant(restaurantId);
        return ResponseEntity.ok(tables);
    }


    //Delete a table
    @CheckRestaurantPermission(IdType.RESTAURANT)
    @DeleteMapping(value = "/delete", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Void> deleteTable(@RequestParam("restaurant_id") Long restaurantId) throws UnauthorizedException {
        tableService.deleteTable(restaurantId);
        return ResponseEntity.noContent().build();
    }
}
