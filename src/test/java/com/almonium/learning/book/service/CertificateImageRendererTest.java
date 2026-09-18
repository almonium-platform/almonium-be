package com.almonium.learning.book.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.learning.book.dto.response.BookCertificateDto;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.List;
import javax.imageio.ImageIO;
import org.junit.jupiter.api.Test;

/** The preview is an exact 1200×630 PNG on cream, whatever the title's length; the sample lands beside the report. */
class CertificateImageRendererTest {
    private final CertificateImageRenderer renderer = new CertificateImageRenderer();

    @Test
    void rendersTheExactCanvasOnCreamPaper() throws IOException {
        byte[] png = renderer.render(certificate("Winnie-the-Pooh"));

        BufferedImage image = ImageIO.read(new ByteArrayInputStream(png));
        assertThat(image.getWidth()).isEqualTo(1200);
        assertThat(image.getHeight()).isEqualTo(630);
        assertThat(image.getRGB(5, 5) & 0xFFFFFF).isEqualTo(0xF9F6F5);
        // The frame's outer hairline runs at the padding.
        assertThat(image.getRGB(600, 28) & 0xFFFFFF).isEqualTo(0x612B5E);
        Files.createDirectories(Path.of("target/certificate"));
        Files.write(Path.of("target/certificate/og-sample.png"), png);
    }

    @Test
    void aLongTitleWrapsAndPushesNothingOffTheCanvas() throws IOException {
        byte[] png =
                renderer.render(certificate("Frankenstein; or, The Modern Prometheus of the Northern Sea Voyages"));

        BufferedImage image = ImageIO.read(new ByteArrayInputStream(png));
        assertThat(image.getWidth()).isEqualTo(1200);
        Files.createDirectories(Path.of("target/certificate"));
        Files.write(Path.of("target/certificate/og-long-title.png"), png);
    }

    private static BookCertificateDto certificate(String title) {
        return new BookCertificateDto(
                "marta",
                "winnie-the-pooh",
                title,
                "A. A. Milne",
                Language.EN,
                List.of(
                        "Heffalump",
                        "Cunning Trap",
                        "wishing",
                        "sorrowful",
                        "hummed",
                        "tremendous",
                        "anxious",
                        "stoutness",
                        "expedition",
                        "crumbs",
                        "scuffle",
                        "plodding"),
                24610,
                187,
                Instant.parse("2026-09-18T10:00:00Z"),
                true);
    }
}
