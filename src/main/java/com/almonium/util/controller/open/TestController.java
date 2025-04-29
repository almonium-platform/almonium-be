package com.almonium.util.controller.open;

import static lombok.AccessLevel.PRIVATE;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Hidden
@RestController
@RequestMapping("/public")
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class TestController {

    @GetMapping("/test")
    public ResponseEntity<?> test() {
        // TODO: test your code here
        return ResponseEntity.ok().build();
    }
}
