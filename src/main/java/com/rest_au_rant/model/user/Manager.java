package com.rest_au_rant.model.user;

import com.rest_au_rant.model.Role;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.*;


@NoArgsConstructor
@Entity
@DiscriminatorValue("MANAGER")
public class Manager extends User {
    public Manager(User user) {
        this.setId(user.getId());
        this.setEmail(user.getEmail());
        this.setUserName(user.getUserName());
        this.setRole(Role.MANAGER);
    }

}
