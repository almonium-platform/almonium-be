package com.almonium.auth.common.controller.open;

import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/public/csrf")
public class CsrfController {

    @GetMapping("/token")
    public CsrfToken csrf(CsrfToken token) {
        return token;
    }
}
