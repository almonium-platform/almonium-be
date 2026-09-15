package com.almonium.subscription.controller.open;

import com.almonium.subscription.dto.response.FoundingMemberStatusDto;
import com.almonium.subscription.service.FoundingMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/public/founding-members")
@RequiredArgsConstructor
public class FoundingMemberController {
    private final FoundingMemberService foundingMemberService;

    @GetMapping
    public ResponseEntity<FoundingMemberStatusDto> status() {
        return ResponseEntity.ok(foundingMemberService.status());
    }
}
