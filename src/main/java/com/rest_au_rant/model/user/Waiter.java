package com.rest_au_rant.model.user;

import com.rest_au_rant.model.Role;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.*;


//@AllArgsConstructor
@NoArgsConstructor
@Entity
@DiscriminatorValue("WAITER")
public class Waiter extends User {

    public Waiter (Long id, String email, String userName, Role role) {
        super(id, userName, email,role);
    }


}

