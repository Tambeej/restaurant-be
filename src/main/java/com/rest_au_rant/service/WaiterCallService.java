package com.rest_au_rant.service;

import com.rest_au_rant.exception.ResourceNotFoundException;
import com.rest_au_rant.model.*;
import com.rest_au_rant.repository.WaiterAssignmentRepository;
import com.rest_au_rant.repository.WaiterCallRepository;
import com.rest_au_rant.repository.WaiterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WaiterCallService {

    private final WaiterAssignmentRepository waiterAssignmentRepository;
    private final WaiterCallRepository waiterCallRepository;
    private final WaiterRepository waiterRepository;

    public WaiterCall createCall(Long assignmentId, CallType callType) {
        WaiterAssignment assignment = waiterAssignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Assignment not found"));

        WaiterCall waiterCall = WaiterCall.builder()
                .assignment(assignment)
                .callType(callType)
                .status(OrderStatus.ORDERED) // New calls are always ordered
                .build();

        return waiterCallRepository.save(waiterCall);
    }



    public List<WaiterCall> getWaiterCallsByWaiter(Long waiterId) {
        return waiterCallRepository.findByAssignment_Waiter_Id(waiterId);
    }

    public List<WaiterCall> getWaiterCallsByTable(Long tableId) {
        return waiterCallRepository.findByAssignment_Table_Id(tableId);
    }

    public void deleteCall(Long callId) {
        waiterCallRepository.deleteById(callId);
    }

    public WaiterCall updateCall(Long callId, OrderStatus status) {
        WaiterCall updatedCall = waiterCallRepository.findById(callId)
                .orElseThrow(() -> new ResourceNotFoundException("Call not found"));
        updatedCall.setStatus(status);
        return waiterCallRepository.save(updatedCall);
    }
}
