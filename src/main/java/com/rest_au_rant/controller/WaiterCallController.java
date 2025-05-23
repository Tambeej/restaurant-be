package com.rest_au_rant.controller;

import com.rest_au_rant.annotation.CheckRestaurantPermission;
import com.rest_au_rant.exception.ResourceNotFoundException;
import com.rest_au_rant.model.*;
import com.rest_au_rant.repository.TableRepository;
import com.rest_au_rant.service.WaiterCallService;
import com.rest_au_rant.service.UserService;
import com.rest_au_rant.service.TableService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/waiter-calls")
@RequiredArgsConstructor
public class WaiterCallController {

    private final WaiterCallService waiterCallService;
    private final UserService userService;
    private final TableRepository tableRepository;

    @PostMapping("/call")
    public ResponseEntity<WaiterCall> createCall(@RequestParam("assignment_id") Long assignmentId,
                                                 @RequestParam("call_type") CallType callType) {
        WaiterCall waiterCall = waiterCallService.createCall(assignmentId, callType);
        return ResponseEntity.ok(waiterCall);
    }


    @GetMapping("/waiter/{waiterId}")
    public ResponseEntity<List<WaiterCall>> getCallsByWaiter(@PathVariable Long waiterId) {
        return ResponseEntity.ok(waiterCallService.getWaiterCallsByWaiter(waiterId));
    }

    @GetMapping("/table/{tableId}")
    public ResponseEntity<List<WaiterCall>> getCallsByTable(@PathVariable Long tableId) {
        return ResponseEntity.ok(waiterCallService.getWaiterCallsByTable(tableId));
    }

    //Update a call
    @PutMapping(value = "/update/{callId}", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<WaiterCall> updateCall(@PathVariable("callId") Long callId,
                                                 @RequestParam("order_status") OrderStatus status) {
        WaiterCall updatedCall = waiterCallService.updateCall(callId, status);
        return ResponseEntity.ok(updatedCall);
    }

    @DeleteMapping("/{callId}")
    public ResponseEntity<Void> deleteCall(@PathVariable Long callId) {
        waiterCallService.deleteCall(callId);
        return ResponseEntity.noContent().build();
    }
}
