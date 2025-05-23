package com.rest_au_rant.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.rest_au_rant.model.user.Kitchen;
import com.rest_au_rant.model.user.Manager;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.*;

@ToString
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "restaurants")
public class Restaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(value = "id")
    private long id;

    @Column(nullable = false, unique = true)
    @JsonProperty(value = "name")
    private String name;

    @JsonProperty(value = "manager_id")
    @OneToOne
    @JoinColumn(name = "manager_id", nullable = false)
    private Manager manager;


    @JsonProperty(value = "Kitchen_id")
    @OneToOne
    @JoinColumn(name = "Kitchen_id")
    private Kitchen kitchen;

}
