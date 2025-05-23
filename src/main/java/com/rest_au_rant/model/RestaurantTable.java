package com.rest_au_rant.model;

import com.fasterxml.jackson.annotation.JsonProperty;
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
@Table(name = "tables")
public class RestaurantTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    @JsonProperty(value = "id")
    private long id;

    @ManyToOne
    @JoinColumn(name = "restaurant_id", nullable = false)
    @JsonProperty(value = "restaurantid")
    private Restaurant restaurant;

    @Column(nullable = false)
    @JsonProperty(value = "tablenumber")
    private int tableNumber;

    @Column(name = "qrcode_path")
    @JsonProperty(value = "qrcode_path")
    private String QRCodePath;


}
