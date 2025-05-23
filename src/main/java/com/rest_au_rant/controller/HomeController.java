package com.rest_au_rant.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class HomeController {

    @GetMapping("home")
    public ResponseEntity<String> home() {
        return ResponseEntity.ok("Welcome to the home page!");
    }
}
