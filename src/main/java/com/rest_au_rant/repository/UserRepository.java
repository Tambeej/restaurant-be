package com.rest_au_rant.repository;

import com.rest_au_rant.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {
//    Optional<User> findById(Long id);
    Optional<User> findByEmail(String email);

    Optional<User> getUserByEmail(String email);
}
