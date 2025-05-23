package com.rest_au_rant.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;




@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "dishes")
public class Dish {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(value = "id")
    private Long id;

    @Column(nullable = false)
    @JsonProperty(value = "name")
    private String name;

    @PositiveOrZero(message = "Price must be 0 or greater")
    @Column(nullable = false)
    @JsonProperty(value = "price")
    private double price;

    @Enumerated(EnumType.STRING)
    @JsonProperty(value = "category")
    private DishCategory category;

    @ManyToOne
    @JoinColumn(name = "restaurant_id", nullable = false)
    @JsonProperty(value = "restaurant_id")
    private Restaurant restaurant;
}


