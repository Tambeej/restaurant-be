package com.rest_au_rant.controller;

import com.rest_au_rant.model.user.User;
import com.rest_au_rant.service.UserService;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@ToString
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<User>> getUsers(@AuthenticationPrincipal OAuth2User user) {
        List<User> allUsers = userService.getAllUsers();
        return ResponseEntity.ok(allUsers);
    }

    @GetMapping("/userinfo")
    public ResponseEntity<User> getUserInfo(@AuthenticationPrincipal Jwt jwt) {
        String email = jwt.getClaim("email");
        return ResponseEntity.ok(userService.getUserInfo(email));
    }

    @GetMapping("/username")
    public ResponseEntity<User> getUserName(@AuthenticationPrincipal Jwt jwt) {
        String email = jwt.getClaim("email");
        return ResponseEntity.ok(userService.getUserInfo(email));
    }


    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody User user) {
        return ResponseEntity.ok(userService.createUser(user));
    }


    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        Optional<User> user = userService.getUserById(id);
        return user.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/user")
    public ResponseEntity<?> getUserInfo(@AuthenticationPrincipal OAuth2User user) {
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not authenticated");
        }
        return ResponseEntity.ok(user.getAttributes());
    }

    @PostMapping("/google")
    public ResponseEntity<User> loginWithGoogle(@AuthenticationPrincipal Jwt jwt) {
        String email = jwt.getClaimAsString("email");
        String name = jwt.getClaimAsString("name");
        User user = userService.findOrCreateByEmail(email, name);
        return ResponseEntity.ok(new User(user.getId(), user.getUserName(), user.getEmail(), user.getRole()));
    }
}
