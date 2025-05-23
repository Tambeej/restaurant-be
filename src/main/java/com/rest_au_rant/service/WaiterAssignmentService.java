package com.rest_au_rant.service;

import com.rest_au_rant.exception.ResourceNotFoundException;
import com.rest_au_rant.exception.RestaurantException;
import com.rest_au_rant.model.*;
import com.rest_au_rant.model.user.User;
import com.rest_au_rant.model.user.Waiter;
import com.rest_au_rant.repository.TableRepository;
import com.rest_au_rant.repository.UserRepository;
import com.rest_au_rant.repository.WaiterAssignmentRepository;
import com.rest_au_rant.repository.WaiterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WaiterAssignmentService {
    private final WaiterAssignmentRepository waiterAssignmentRepository;
    private final TableRepository tableRepository;
    private final UserRepository userRepository;
    private final WaiterRepository waiterRepository;

    @Autowired
    public WaiterAssignmentService(WaiterAssignmentRepository waiterAssignmentRepository,
                                   TableRepository tableRepository,
                                   UserRepository userRepository, WaiterRepository waiterRepository) {

        this.waiterAssignmentRepository = waiterAssignmentRepository;
        this.tableRepository = tableRepository;
        this.userRepository = userRepository;
        this.waiterRepository = waiterRepository;
    }

    public WaiterAssignment assignWaiterToTable(Long tableId, Long waiterId) {

        RestaurantTable table = tableRepository.findById(tableId)
                .orElseThrow(()-> new ResourceNotFoundException("Error: Table with ID " + tableId + " not found."));

        Waiter waiter = waiterRepository.findById(waiterId)
                .orElseThrow(()-> new ResourceNotFoundException("Error: Waiter with ID " + waiterId + " not found."));

        if (waiter.getRole() != Role.WAITER) {
            throw new RestaurantException("Error: User " + waiter.getUserName() + " is not a waiter.");
        }

        Long tableRestaurantId = table.getRestaurant().getId();
        Long waiterRestaurantId = waiterRepository.findRestaurantIdByWaiterId(waiterId);

        if (!tableRestaurantId.equals(waiterRestaurantId)) {
            throw new RestaurantException("Error: Waiter " + waiter.getUserName() + " doesnt work in restaurant "+tableRestaurantId+".");
        }
        WaiterAssignment assignment = new WaiterAssignment();
        assignment.setTable(table);
        assignment.setWaiter(waiter);
        //Check if waiter is already assigned to that table
        List<WaiterAssignment> assignedTables = waiterAssignmentRepository.findByWaiter(waiter);
        if (assignedTables!= null && (!assignedTables.contains(assignment))) {
            throw new ResourceNotFoundException("Table assigned to waiter");
        }
        return waiterAssignmentRepository.save(assignment);
    }

    public List<WaiterAssignment> getTablesByWaiter(Long waiterId) {
        User waiter = userRepository.findById(waiterId)
                .orElseThrow(()-> new ResourceNotFoundException("Error: Waiter " + waiterId + " does not exists."));

        if (waiter.getRole() != Role.WAITER) {
            throw new RestaurantException("Error: User " + waiter.getUserName() + " is not a waiter.");
        }

        return waiterAssignmentRepository.findByWaiter(waiter);
    }

    public WaiterAssignment getWaiterByTable(Long tableId) {
        RestaurantTable table = tableRepository.findById(tableId)
                .orElseThrow(()-> new ResourceNotFoundException("Error: Table " + tableId + " does not exists."));

        return waiterAssignmentRepository.findByTable(table);
    }

    public void removeWaiterFromTable(Long assignmentId) {
        if (!waiterAssignmentRepository.existsById(assignmentId)) {
            throw new RuntimeException("Error: Assignment " + assignmentId + " does not exists.");
        }
        waiterAssignmentRepository.deleteById(assignmentId);
    }

    public List<WaiterAssignment> getAllAssignments() {
        return waiterAssignmentRepository.findAll();
    }

}
