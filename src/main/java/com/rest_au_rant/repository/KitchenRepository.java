package com.rest_au_rant.repository;

import com.rest_au_rant.model.user.Kitchen;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KitchenRepository  extends JpaRepository<Kitchen, Long> {

}
