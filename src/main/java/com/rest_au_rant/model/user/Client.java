package com.rest_au_rant.model.user;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;


@Entity
@DiscriminatorValue("CLIENT")
public class Client extends User {
}
