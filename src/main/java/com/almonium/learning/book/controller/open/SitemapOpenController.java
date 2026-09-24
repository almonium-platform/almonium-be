package com.almonium.learning.book.controller.open;

import com.almonium.learning.book.service.BookSitemapService;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.concurrent.TimeUnit;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/** The sitemap the app domain's /sitemap.xml proxies to; robots.txt on that domain points at it. */
@Tag(name = "Books", description = "Operations related to discovering, managing, and interacting with books.")
@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SitemapOpenController {
    BookSitemapService sitemapService;

    @GetMapping(value = "/public/sitemap.xml", produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<String> sitemap() {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_XML)
                .cacheControl(CacheControl.maxAge(1, TimeUnit.HOURS).cachePublic())
                .body(sitemapService.sitemap());
    }
}
