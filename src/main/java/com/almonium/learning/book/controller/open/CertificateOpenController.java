package com.almonium.learning.book.controller.open;

import com.almonium.config.properties.AppProperties;
import com.almonium.learning.book.dto.response.BookCertificateDto;
import com.almonium.learning.book.service.BookCertificateService;
import com.almonium.learning.book.service.CertificateImageRenderer;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.HtmlUtils;

/**
 * The public certificate (design K1, K2): the record the page renders, the image a link to it unfurls into, and
 * the bare HTML a crawler is handed in place of the app shell so the unfurl has its title, words and image. All
 * three are 404 while the reader keeps the page off.
 */
@Tag(name = "Books", description = "Operations related to discovering, managing, and interacting with books.")
@RestController
@RequestMapping("/public/certificates/{username}/{editionSlug}")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CertificateOpenController {
    BookCertificateService certificateService;
    CertificateImageRenderer imageRenderer;
    AppProperties appProperties;

    @GetMapping
    public ResponseEntity<BookCertificateDto> get(@PathVariable String username, @PathVariable String editionSlug) {
        return ResponseEntity.ok(certificateService.publicView(username, editionSlug));
    }

    @GetMapping(value = "/og.png", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> image(@PathVariable String username, @PathVariable String editionSlug) {
        BookCertificateDto certificate = certificateService.publicView(username, editionSlug);
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .cacheControl(CacheControl.maxAge(1, TimeUnit.HOURS).cachePublic())
                .body(imageRenderer.render(certificate));
    }

    /** What a link scraper reads: the same title, description and image the page carries, and nothing more. */
    @GetMapping(value = "/page", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<String> page(@PathVariable String username, @PathVariable String editionSlug) {
        BookCertificateDto certificate = certificateService.publicView(username, editionSlug);
        String pageUrl =
                appProperties.getWebDomain() + "/read/@" + certificate.username() + "/" + certificate.editionSlug();
        String title = pageTitle(certificate);
        String description = String.join(", ", certificate.words());
        String html = """
                <!doctype html>
                <html lang="en">
                <head>
                <meta charset="utf-8">
                <title>%1$s</title>
                <meta name="description" content="%2$s">
                <link rel="canonical" href="%3$s">
                <meta property="og:type" content="website">
                <meta property="og:site_name" content="Almonium">
                <meta property="og:title" content="%1$s">
                <meta property="og:description" content="%2$s">
                <meta property="og:url" content="%3$s">
                <meta property="og:image" content="%3$s/og.png">
                <meta property="og:image:width" content="%4$d">
                <meta property="og:image:height" content="%5$d">
                <meta name="twitter:card" content="summary_large_image">
                </head>
                <body>
                <h1>%1$s</h1>
                <p>%2$s</p>
                <p><a href="%3$s">%3$s</a></p>
                </body>
                </html>
                """.formatted(
                        HtmlUtils.htmlEscape(title),
                        HtmlUtils.htmlEscape(description),
                        HtmlUtils.htmlEscape(pageUrl),
                        CertificateImageRenderer.WIDTH,
                        CertificateImageRenderer.HEIGHT);
        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_HTML)
                .cacheControl(CacheControl.maxAge(1, TimeUnit.HOURS).cachePublic())
                .body(html);
    }

    /** "@marta read Winnie-the-Pooh in English · Almonium". */
    static String pageTitle(BookCertificateDto certificate) {
        String language = Locale.forLanguageTag(certificate.language().name().toLowerCase(Locale.ROOT))
                .getDisplayLanguage(Locale.ENGLISH);
        if (language.isEmpty()) language = certificate.language().name();
        return "@" + certificate.username() + " read " + certificate.title() + " in " + language + " · Almonium";
    }
}
