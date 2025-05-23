package com.rest_au_rant.controller;

import com.rest_au_rant.annotation.CheckRestaurantPermission;
import com.rest_au_rant.model.IdType;
import com.rest_au_rant.model.WaiterAssignment;
import com.rest_au_rant.service.WaiterAssignmentService;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@ToString
@RestController
@RequestMapping("/api/waiter-assignments")
public class WaiterAssignmentController {

    private final WaiterAssignmentService waiterAssignmentService;

    @Autowired
    public WaiterAssignmentController(WaiterAssignmentService waiterAssignmentService) {
        this.waiterAssignmentService = waiterAssignmentService;
    }

    // Assign a waiter to a table
    @CheckRestaurantPermission(IdType.TABLE)
    @PostMapping("/{tableId}/{waiterId}")
    public ResponseEntity<?> assignWaiterToTable(
            @PathVariable Long tableId, @PathVariable Long waiterId) {
        return ResponseEntity.ok(waiterAssignmentService.assignWaiterToTable(tableId, waiterId));
    }

    // Get all assignments
    @GetMapping
    public ResponseEntity<List<WaiterAssignment>> getAllAssignments() {
        return ResponseEntity.ok(waiterAssignmentService.getAllAssignments());
    }

    // Get tables assigned to a specific waiter
    @CheckRestaurantPermission(IdType.WAITER)
    @GetMapping("/waiter/{waiterId}")
    public ResponseEntity<List<WaiterAssignment>> getTablesByWaiter(@PathVariable Long waiterId) {
        return ResponseEntity.ok(waiterAssignmentService.getTablesByWaiter(waiterId));
    }

    // Get the waiter assigned to a specific table
    @CheckRestaurantPermission(IdType.TABLE)
    @GetMapping("/table/{tableId}")
    public ResponseEntity<?> getWaiterByTable(@PathVariable Long tableId) {
        return ResponseEntity.ok(waiterAssignmentService.getWaiterByTable(tableId));

    }

    // Remove a waiter from a table
    @CheckRestaurantPermission(IdType.ASSIGNMENT)
    @DeleteMapping("/{assignmentId}")
    public ResponseEntity<Void> removeWaiterFromTable(@PathVariable Long assignmentId) {
        waiterAssignmentService.removeWaiterFromTable(assignmentId);
        return ResponseEntity.noContent().build();
    }
}
