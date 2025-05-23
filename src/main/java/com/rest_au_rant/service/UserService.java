package com.rest_au_rant.service;

import com.rest_au_rant.model.user.Client;
import com.rest_au_rant.model.user.Kitchen;
import com.rest_au_rant.model.user.Manager;
import com.rest_au_rant.model.user.User;
import com.rest_au_rant.model.user.Waiter;
import com.rest_au_rant.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import com.rest_au_rant.model.Role;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public User processOAuthPostLogin(OAuth2User oAuth2User) {
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");
        return findOrCreateByEmail(email,name);
    }
    public User findOrCreateByEmail(String email,String name) {
        Optional<User> existingUser = userRepository.findByEmail(email);

        if (existingUser.isPresent()) {
            return existingUser.get();
        } else {
            // Create new user as CLIENT by default
            User newUser = new Client();
            newUser.setEmail(email);
            newUser.setUserName(name); // Or fetch from JWT "name" if needed
            newUser.setRole(Role.CLIENT);
            return userRepository.save(newUser);
        }
    }


    public User getUserInfo(String email) {
        // Look for the user in your DB
        Optional<User> optionalUser = userRepository.getUserByEmail(email);

        // Auto-register the user as CLIENT if not found

        return optionalUser.orElseGet(() -> {
            User newUser = new Client();
            newUser.setEmail(email);
            newUser.setUserName(userRepository.findByEmail(email).get().getUserName());
            newUser.setRole(Role.CLIENT);
            return userRepository.save(newUser);
        });



    }
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}

