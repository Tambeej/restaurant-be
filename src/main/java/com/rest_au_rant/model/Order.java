package com.rest_au_rant.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.rest_au_rant.model.user.Client;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @ManyToOne
    @JoinColumn(name = "table_id", nullable = false)
    private RestaurantTable table;

    @Builder.Default
    @Temporal(TemporalType.TIMESTAMP)
    @JsonProperty(value = "order_date")
    private final LocalDateTime orderDate = LocalDateTime.now();


}
