package com.rest_au_rant.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "waiter_calls")
public class WaiterCall {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "assignment_id", nullable = false)
    private WaiterAssignment assignment;  // <-- Reference to assignment

    @Enumerated(EnumType.STRING)
    @JsonProperty(value = "call_type")
    private CallType callType;

    @Enumerated(EnumType.STRING)
    @JsonProperty(value = "status")
    private OrderStatus status;
}
