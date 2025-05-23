package com.rest_au_rant.model.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.rest_au_rant.model.Role;
import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@DiscriminatorColumn(name = "role")
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(value = "id")
    private long id;

    @Column(nullable = false, unique = true)
    @JsonProperty(value = "username")
    private String userName;

    @Column(nullable = false, unique = true)
    @JsonProperty(value = "email")
    private String email;


    // Explicit getter
    @Getter
    @Enumerated(EnumType.STRING)
    @Column(name = "role",insertable=false, updatable=false,nullable = false)
    @JsonProperty(value = "role")
    private Role role;


}