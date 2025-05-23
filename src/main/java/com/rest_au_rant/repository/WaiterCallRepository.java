package com.rest_au_rant.repository;

import com.rest_au_rant.model.WaiterCall;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WaiterCallRepository extends JpaRepository<WaiterCall, Long> {

    List<WaiterCall> findByAssignment_Waiter_Id(Long waiterId);

    List<WaiterCall> findByAssignment_Table_Id(Long tableId);
}

