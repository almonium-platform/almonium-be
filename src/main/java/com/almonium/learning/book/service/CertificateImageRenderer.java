package com.almonium.learning.book.service;

import com.almonium.learning.book.dto.response.BookCertificateDto;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.imageio.ImageIO;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

/**
 * The link preview (design K2): the certificate re-flowed to 1200×630 so the twelve words survive the crop every
 * feed applies. Cream paper edge to edge, the double hairline as the frame, the title block on the left and the
 * words on the right. Drawn with the product's own faces, bundled here, so the image is the same on every host.
 */
@Component
public class CertificateImageRenderer {
    public static final int WIDTH = 1200;
    public static final int HEIGHT = 630;

    private static final Color PAPER = new Color(0xF9F6F5);
    private static final Color FRAME = new Color(0x612B5E);
    private static final Color INK = new Color(0x2C2530);
    private static final Color PLUM = new Color(0x872657);
    private static final Color SUBHEAD = new Color(0x5F5560);
    private static final Color METADATA = new Color(0xA99AA8);

    private static final int PADDING = 28;
    private static final int FRAME_GAP = 4;
    private static final int CONTENT_X = 64;
    private static final int CONTENT_Y = 56;
    private static final int COLUMN_GAP = 64;
    private static final int WORDS_INSET = 56;
    /** Past this many characters the title steps down one size so two lines still fit the column. */
    private static final int LONG_TITLE = 22;

    private final Font serif;
    private final Font serifSemibold;
    private final Font serifItalic;
    private final Font mono;
    private final BufferedImage logoMark;
    private final BufferedImage wordmark;

    public CertificateImageRenderer() {
        serif = load("certificate/fonts/Literata-Regular.ttf");
        serifSemibold = load("certificate/fonts/Literata-SemiBold.ttf");
        serifItalic = load("certificate/fonts/Literata-Italic.ttf");
        mono = load("certificate/fonts/IBMPlexMono-Regular.ttf");
        logoMark = image("certificate/logo-mark.png");
        wordmark = image("certificate/wordmark.png");
    }

    public byte[] render(BookCertificateDto certificate) {
        BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        try {
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g.setColor(PAPER);
            g.fillRect(0, 0, WIDTH, HEIGHT);
            drawFrame(g);

            int contentLeft = PADDING + 1 + FRAME_GAP + 1 + CONTENT_X;
            int contentTop = PADDING + 1 + FRAME_GAP + 1 + CONTENT_Y;
            int contentWidth = WIDTH - 2 * contentLeft;
            int contentHeight = HEIGHT - 2 * contentTop;
            int columnWidth = (contentWidth - COLUMN_GAP) / 2;

            Block title = titleBlock(g, certificate, columnWidth);
            Block words = wordsBlock(g, certificate, columnWidth - WORDS_INSET - 1);
            int rowHeight = Math.max(title.height, words.height);
            int rowTop = contentTop + (contentHeight - rowHeight) / 2;

            title.draw(g, contentLeft, rowTop + (rowHeight - title.height) / 2);
            int wordsLeft = contentLeft + columnWidth + COLUMN_GAP;
            g.setColor(FRAME);
            g.fillRect(wordsLeft, rowTop, 1, rowHeight);
            words.draw(g, wordsLeft + 1 + WORDS_INSET, rowTop + (rowHeight - words.height) / 2);
        } finally {
            g.dispose();
        }
        return toPng(image);
    }

    /** Double hairline: the outer line at the padding, the inner one four pixels in. */
    private void drawFrame(Graphics2D g) {
        g.setColor(FRAME);
        g.drawRect(PADDING, PADDING, WIDTH - 2 * PADDING - 1, HEIGHT - 2 * PADDING - 1);
        int inner = PADDING + 1 + FRAME_GAP;
        g.drawRect(inner, inner, WIDTH - 2 * inner - 1, HEIGHT - 2 * inner - 1);
    }

    /** Mark, eyebrow, title, author line, rule, counts, wordmark: the left column, top to bottom. */
    private Block titleBlock(Graphics2D g, BookCertificateDto certificate, int width) {
        Block block = new Block();
        block.image(logoMark, 56, 56);
        block.gap(22);
        block.text(tracked(mono.deriveFont(15f), 3f), PLUM, "READ TO THE END", 18);
        block.gap(12);
        float titleSize = certificate.title().length() > LONG_TITLE ? 44f : 52f;
        Font titleFont = serifSemibold.deriveFont(titleSize);
        for (String line : wrap(g.getFontMetrics(titleFont), certificate.title(), width, 2)) {
            block.text(titleFont, INK, line, Math.round(titleSize * 1.08f));
        }
        block.gap(12);
        Font italic = serifItalic.deriveFont(21f);
        block.text(italic, SUBHEAD, certificate.author() + " · " + languageName(certificate), 29);
        block.run(
                SUBHEAD, 29, new Run(italic, "read by "), new Run(serif.deriveFont(21f), "@" + certificate.username()));
        block.gap(22);
        block.rule(120);
        block.gap(22);
        NumberFormat count = NumberFormat.getIntegerInstance(Locale.US);
        Font stat = serif.deriveFont(19f);
        Font statStrong = serifSemibold.deriveFont(19f);
        block.run(
                SUBHEAD,
                24,
                new Run(statStrong, INK, count.format(certificate.wordsRead())),
                new Run(stat, " words read"),
                new Run(stat, "        "),
                new Run(statStrong, INK, count.format(certificate.wordsSaved())),
                new Run(stat, " saved"));
        block.gap(22);
        block.image(wordmark, Math.round(20f * wordmark.getWidth() / wordmark.getHeight()), 20, .85f);
        return block;
    }

    /** The label and the twelve words in two columns: the right column, which is what a feed's crop keeps. */
    private Block wordsBlock(Graphics2D g, BookCertificateDto certificate, int width) {
        Block block = new Block();
        block.text(tracked(mono.deriveFont(13f), 2f), METADATA, "THE TWELVE RAREST WORDS", 16);
        block.gap(20);
        int columnGap = 28;
        int cell = (width - columnGap) / 2;
        List<String> words = certificate.words();
        for (int row = 0; row < (words.size() + 1) / 2; row++) {
            String left = words.get(row * 2);
            String right = row * 2 + 1 < words.size() ? words.get(row * 2 + 1) : "";
            block.cells(g, serif, 26f, INK, 31, cell, columnGap, left, right);
            if (row * 2 + 2 < words.size()) block.gap(14);
        }
        return block;
    }

    private static String languageName(BookCertificateDto certificate) {
        String name = Locale.forLanguageTag(certificate.language().name().toLowerCase(Locale.ROOT))
                .getDisplayLanguage(Locale.ENGLISH);
        return name.isEmpty() ? certificate.language().name() : name;
    }

    /** Greedy word wrap into at most {@code maxLines}; whatever does not fit ends the last line with an ellipsis. */
    static List<String> wrap(FontMetrics metrics, String text, int width, int maxLines) {
        List<String> lines = new ArrayList<>();
        StringBuilder line = new StringBuilder();
        for (String word : text.trim().split("\\s+")) {
            String candidate = line.isEmpty() ? word : line + " " + word;
            if (metrics.stringWidth(candidate) <= width || line.isEmpty()) {
                line = new StringBuilder(candidate);
            } else {
                lines.add(line.toString());
                line = new StringBuilder(word);
            }
        }
        lines.add(line.toString());
        if (lines.size() > maxLines) {
            lines = new ArrayList<>(lines.subList(0, maxLines));
            String last = lines.get(maxLines - 1);
            while (!last.isEmpty() && metrics.stringWidth(last + "…") > width) {
                last = last.substring(0, last.length() - 1).stripTrailing();
            }
            lines.set(maxLines - 1, last + "…");
        }
        return lines;
    }

    /** A font shrunk until the text fits the width, never below 18px: a long word is drawn whole, not cut. */
    private static Font fitted(Graphics2D g, Font base, float size, String text, int width) {
        Font font = base.deriveFont(size);
        while (size > 18f && g.getFontMetrics(font).stringWidth(text) > width) {
            size -= 1f;
            font = base.deriveFont(size);
        }
        return font;
    }

    private static Font tracked(Font font, float pixels) {
        return font.deriveFont(Map.of(TextAttribute.TRACKING, pixels / font.getSize2D()));
    }

    private static Font load(String path) {
        try (InputStream stream = new ClassPathResource(path).getInputStream()) {
            return Font.createFont(Font.TRUETYPE_FONT, stream);
        } catch (Exception e) {
            throw new IllegalStateException("Certificate font missing: " + path, e);
        }
    }

    private static BufferedImage image(String path) {
        try (InputStream stream = new ClassPathResource(path).getInputStream()) {
            BufferedImage image = ImageIO.read(stream);
            if (image == null) throw new IllegalStateException("Not an image: " + path);
            return image;
        } catch (IOException e) {
            throw new IllegalStateException("Certificate image missing: " + path, e);
        }
    }

    private static byte[] toPng(BufferedImage image) {
        try (ByteArrayOutputStream stream = new ByteArrayOutputStream()) {
            ImageIO.write(image, "png", stream);
            return stream.toByteArray();
        } catch (IOException e) {
            throw new IllegalStateException("Could not encode the certificate image", e);
        }
    }

    private record Run(Font font, Color color, String text) {
        Run(Font font, String text) {
            this(font, null, text);
        }
    }

    /** A vertical stack of drawables measured before it is placed, so a column can be centred as a whole. */
    private static final class Block {
        private final List<Drawable> items = new ArrayList<>();
        int height;

        void gap(int pixels) {
            height += pixels;
            items.add((g, x, y) -> {});
        }

        void text(Font font, Color color, String text, int lineHeight) {
            run(color, lineHeight, new Run(font, text));
        }

        /** One line of mixed runs, each in its own face; a run without a colour takes the line's. */
        void run(Color color, int lineHeight, Run... runs) {
            int offset = height;
            height += lineHeight;
            items.add((g, x, y) -> {
                float cursor = x;
                for (Run run : runs) {
                    g.setFont(run.font());
                    g.setColor(run.color() == null ? color : run.color());
                    FontMetrics metrics = g.getFontMetrics();
                    float baseline = y + offset + (lineHeight + metrics.getAscent() - metrics.getDescent()) / 2f;
                    g.drawString(run.text(), cursor, baseline);
                    cursor += metrics.stringWidth(run.text());
                }
            });
        }

        /** Two words on one row, each fitted to its cell. */
        void cells(
                Graphics2D g,
                Font base,
                float size,
                Color color,
                int lineHeight,
                int cell,
                int gap,
                String left,
                String right) {
            Font leftFont = fitted(g, base, size, left, cell);
            Font rightFont = fitted(g, base, size, right, cell);
            int offset = height;
            height += lineHeight;
            items.add((graphics, x, y) -> {
                graphics.setColor(color);
                drawCell(graphics, leftFont, left, x, y + offset, lineHeight);
                drawCell(graphics, rightFont, right, x + cell + gap, y + offset, lineHeight);
            });
        }

        private static void drawCell(Graphics2D g, Font font, String text, int x, int top, int lineHeight) {
            g.setFont(font);
            FontMetrics metrics = g.getFontMetrics();
            g.drawString(text, x, top + (lineHeight + metrics.getAscent() - metrics.getDescent()) / 2f);
        }

        void rule(int width) {
            int offset = height;
            height += 1;
            items.add((g, x, y) -> {
                g.setColor(new Color(FRAME.getRed(), FRAME.getGreen(), FRAME.getBlue(), 128));
                g.fillRect(x, y + offset, width, 1);
            });
        }

        void image(BufferedImage image, int width, int imageHeight) {
            image(image, width, imageHeight, 1f);
        }

        void image(BufferedImage image, int width, int imageHeight, float opacity) {
            int offset = height;
            height += imageHeight;
            items.add((g, x, y) -> {
                var composite = g.getComposite();
                g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
                g.drawImage(image, x, y + offset, width, imageHeight, null);
                g.setComposite(composite);
            });
        }

        void draw(Graphics2D g, int x, int y) {
            for (Drawable item : items) item.draw(g, x, y);
        }
    }

    private interface Drawable {
        void draw(Graphics2D g, int x, int y);
    }
}
