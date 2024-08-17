package com.almonium.util.controller;

import static lombok.AccessLevel.PRIVATE;

import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/system-info")
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class SystemInfoController {
    Environment environment;

    @GetMapping("/active-profiles")
    public ResponseEntity<List<String>> getCurrentActiveProfiles() {
        return ResponseEntity.ok(Arrays.asList(environment.getActiveProfiles()));
    }
}
