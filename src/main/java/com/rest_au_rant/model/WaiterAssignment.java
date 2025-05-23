package com.rest_au_rant.model;

import com.rest_au_rant.model.user.Waiter;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "waiters_tables")
public class WaiterAssignment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "table_id", nullable = false)
    private RestaurantTable table;

    @ManyToOne
    @JoinColumn(name = "waiter_id", nullable = false)
    private Waiter waiter;

}
