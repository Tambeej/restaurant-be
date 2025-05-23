package com.rest_au_rant.model;

import com.rest_au_rant.model.user.Waiter;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "waiter_restaurant")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WaiterRestaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "waiter_id", referencedColumnName = "id", nullable = false)
    private Waiter waiter;

    @ManyToOne
    @JoinColumn(name = "restaurant_id", referencedColumnName = "id", nullable = false)
    private Restaurant restaurant;
}
