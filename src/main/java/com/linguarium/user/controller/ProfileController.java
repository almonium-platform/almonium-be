package com.linguarium.user.controller;

import static lombok.AccessLevel.PRIVATE;

import java.util.Arrays;
import java.util.List;
import lombok.experimental.FieldDefaults;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/all")
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class ProfileController {
    Environment environment;

    public ProfileController(Environment environment) {
        this.environment = environment;
    }

    @GetMapping("/profile")
    public ResponseEntity<List<String>> getCurrentActiveProfiles() {
        return ResponseEntity.ok(Arrays.asList(environment.getActiveProfiles()));
    }
}
