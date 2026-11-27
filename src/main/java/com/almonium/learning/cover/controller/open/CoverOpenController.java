package com.almonium.learning.cover.controller.open;

import com.almonium.learning.cover.dto.CoverArt;
import com.almonium.learning.cover.service.CoverService;
import java.time.Duration;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Cover art for the shelf: which works have some, and the image itself. Open to guests, like the shelf. */
@RestController
@RequestMapping("/public/covers")
@RequiredArgsConstructor
public class CoverOpenController {
    private final CoverService coverService;

    @GetMapping
    public ResponseEntity<List<CoverArt>> covers() {
        return ResponseEntity.ok(coverService.approved());
    }

    @GetMapping("/{workSlug}/image")
    public ResponseEntity<byte[]> image(@PathVariable String workSlug) {
        return coverService
                .image(workSlug)
                .map(cover -> ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(cover.getContentType()))
                        .cacheControl(CacheControl.maxAge(Duration.ofDays(1)).cachePublic())
                        .body(cover.getImage()))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
