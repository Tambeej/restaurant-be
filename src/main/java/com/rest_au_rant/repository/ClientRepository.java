package com.rest_au_rant.repository;

import com.rest_au_rant.model.user.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {

}
