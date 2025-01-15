package com.almonium.infra.qr.service;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.infra.qr.exception.QRCodeGenerationException;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.google.zxing.qrcode.encoder.ByteMatrix;
import com.google.zxing.qrcode.encoder.Encoder;
import com.google.zxing.qrcode.encoder.QRCode;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Arc2D;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import javax.imageio.ImageIO;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class QRCodeGenerator {
    private static final Color BACKGROUND = new Color(255, 255, 255, 0);
    private static final Color COLOR = Color.BLACK;
    private static final int EXCLUSION_RADIUS = 5;
    private static final int SIZE = 500;

    public byte[] generateQRCode(String text) {
        return bufferedImageToBytes(renderQRImage(generateQRCodeInternal(text)));
    }

    private QRCode generateQRCodeInternal(String text) {
        try {
            var hints = Map.of(EncodeHintType.CHARACTER_SET, StandardCharsets.UTF_8.name());
            return Encoder.encode(text, ErrorCorrectionLevel.H, hints);
        } catch (WriterException e) {
            throw new QRCodeGenerationException("Failed to generate QR code", e);
        }
    }

    private byte[] bufferedImageToBytes(BufferedImage image) {
        try (ByteArrayOutputStream stream = new ByteArrayOutputStream()) {
            ImageIO.write(image, "png", stream);
            return stream.toByteArray();
        } catch (IOException e) {
            throw new QRCodeGenerationException("Failed to convert BufferedImage to byte array", e);
        }
    }

    private BufferedImage renderQRImage(QRCode code) {
        // Create a transparent image
        BufferedImage image =
                new BufferedImage(QRCodeGenerator.SIZE, QRCodeGenerator.SIZE, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = image.createGraphics();

        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setComposite(AlphaComposite.Src);
        graphics.setBackground(BACKGROUND);
        graphics.clearRect(0, 0, QRCodeGenerator.SIZE, QRCodeGenerator.SIZE);

        ByteMatrix input = code.getMatrix();
        if (input == null) {
            throw new QRCodeGenerationException("QR code matrix is null");
        }

        int inputWidth = input.getWidth();
        int inputHeight = input.getHeight();
        int moduleSize = Math.min(QRCodeGenerator.SIZE / inputWidth, QRCodeGenerator.SIZE / inputHeight);
        int leftPadding = (QRCodeGenerator.SIZE - (inputWidth * moduleSize)) / 2;
        int topPadding = (QRCodeGenerator.SIZE - (inputHeight * moduleSize)) / 2;

        // Define exclusion zone (center circle)
        int centerX = inputWidth / 2;
        int centerY = inputHeight / 2;

        // Step 1: Remove dots within the exclusion zone
        for (int y = 0; y < inputHeight; y++) {
            for (int x = 0; x < inputWidth; x++) {
                // Calculate distance from center
                double distanceFromCenter = Math.sqrt(Math.pow(x - centerX, 2) + Math.pow(y - centerY, 2));
                if (distanceFromCenter <= EXCLUSION_RADIUS) {
                    input.set(x, y, 0); // Clear the dot
                }
            }
        }

        // Step 2: Draw the QR code with modified data
        for (int inputY = 0, outputY = topPadding; inputY < inputHeight; inputY++, outputY += moduleSize) {
            for (int inputX = 0, outputX = leftPadding; inputX < inputWidth; inputX++, outputX += moduleSize) {

                // Apply rounding logic to corners and edges
                boolean topWhite = inputY == 0 || input.get(inputX, inputY - 1) == 0;
                boolean bottomWhite = inputY == inputHeight - 1 || input.get(inputX, inputY + 1) == 0;
                boolean leftWhite = inputX == 0 || input.get(inputX - 1, inputY) == 0;
                boolean rightWhite = inputX == inputWidth - 1 || input.get(inputX + 1, inputY) == 0;

                boolean bottomBlack = !bottomWhite;
                boolean topBlack = !topWhite;
                boolean leftBlack = !leftWhite;
                boolean rightBlack = !rightWhite;

                boolean topLeftWhite = (inputX == 0 || inputY == 0) || input.get(inputX - 1, inputY - 1) == 0;
                boolean topRightWhite =
                        (inputX == inputWidth - 1 || inputY == 0) || input.get(inputX + 1, inputY - 1) == 0;
                boolean bottomLeftWhite =
                        (inputX == 0 || inputY == inputHeight - 1) || input.get(inputX - 1, inputY + 1) == 0;
                boolean bottomRightWhite = (inputX == inputWidth - 1 || inputY == inputHeight - 1)
                        || input.get(inputX + 1, inputY + 1) == 0;

                boolean topLeftBlack = !topLeftWhite;
                boolean topRightBlack = !topRightWhite;
                boolean bottomLeftBlack = !bottomLeftWhite;
                boolean bottomRightBlack = !bottomRightWhite;

                int arcSize = moduleSize / 2; // Radius for rounding

                if (input.get(inputX, inputY) == 0) {
                    if (leftBlack && topBlack && topLeftBlack) {
                        fillTopLeftQuarterMinusArc(graphics, outputX, outputY, moduleSize, arcSize);
                    }

                    if (rightBlack && topBlack && topRightBlack) {
                        fillTopRightQuarterMinusArc(graphics, outputX, outputY, moduleSize, arcSize);
                    }

                    if (leftBlack && bottomBlack && bottomLeftBlack) {
                        fillBottomLeftQuarterMinusArc(graphics, outputX, outputY, moduleSize, arcSize);
                    }

                    if (rightBlack && bottomBlack && bottomRightBlack) {
                        fillBottomRightQuarterMinusArc(graphics, outputX, outputY, moduleSize, arcSize);
                    }
                }

                if (input.get(inputX, inputY) == 1) {
                    graphics.setColor(COLOR);
                    graphics.fillRect(outputX, outputY, moduleSize, moduleSize);

                    if (topWhite && leftWhite) {
                        graphics.clearRect(outputX, outputY, arcSize, arcSize);
                        graphics.fillArc(outputX, outputY, arcSize * 2, arcSize * 2, 90, 90);
                    }
                    if (topWhite && rightWhite) {
                        graphics.clearRect(outputX + moduleSize - arcSize, outputY, arcSize, arcSize);
                        graphics.fillArc(outputX + moduleSize - arcSize * 2, outputY, arcSize * 2, arcSize * 2, 0, 90);
                    }
                    if (bottomWhite && leftWhite) {
                        graphics.clearRect(outputX, outputY + moduleSize - arcSize, arcSize, arcSize);
                        graphics.fillArc(
                                outputX, outputY + moduleSize - arcSize * 2, arcSize * 2, arcSize * 2, 180, 90);
                    }
                    if (bottomWhite && rightWhite) {
                        graphics.clearRect(
                                outputX + moduleSize - arcSize, outputY + moduleSize - arcSize, arcSize, arcSize);
                        graphics.fillArc(
                                outputX + moduleSize - arcSize * 2,
                                outputY + moduleSize - arcSize * 2,
                                arcSize * 2,
                                arcSize * 2,
                                270,
                                90);
                    }
                }
            }
        }

        int finderPatternSize = moduleSize * 7;
        drawFinderPatternRoundedStyle(graphics, leftPadding, topPadding, finderPatternSize);
        drawFinderPatternRoundedStyle(
                graphics, leftPadding + (inputWidth - 7) * moduleSize, topPadding, finderPatternSize);
        drawFinderPatternRoundedStyle(
                graphics, leftPadding, topPadding + (inputHeight - 7) * moduleSize, finderPatternSize);

        return image;
    }

    private void fillTopLeftQuarterMinusArc(Graphics2D g, int x, int y, int moduleSize, int arcSize) {
        // Create shape for the top-left quarter only
        Area shape = new Area(new Rectangle2D.Double(x, y, moduleSize / 2.0 - 1, moduleSize / 2.0 - 1));

        // Subtract the arc from that quarter
        Arc2D arc = new Arc2D.Double(x, y, arcSize * 2.0, arcSize * 2.0, 90, 90, Arc2D.PIE);
        shape.subtract(new Area(arc));

        // Fill only that quarter minus the arc
        g.fill(shape);
    }

    private void fillTopRightQuarterMinusArc(Graphics2D g, int x, int y, int moduleSize, int arcSize) {
        Area shape = new Area(
                new Rectangle2D.Double(x + moduleSize / 2.0 + 1, y, moduleSize / 2.0 - 1, moduleSize / 2.0 - 1));

        Arc2D arc = new Arc2D.Double(x + moduleSize - arcSize * 2.0, y, arcSize * 2.0, arcSize * 2.0, 0, 90, Arc2D.PIE);
        shape.subtract(new Area(arc));

        g.fill(shape);
    }

    private void fillBottomLeftQuarterMinusArc(Graphics2D g, int x, int y, int moduleSize, int arcSize) {
        Area shape = new Area(
                new Rectangle2D.Double(x, y + moduleSize / 2.0 + 1, moduleSize / 2.0 - 1, moduleSize / 2.0 - 1));

        Arc2D arc =
                new Arc2D.Double(x, y + moduleSize - arcSize * 2.0, arcSize * 2.0, arcSize * 2.0, 180, 90, Arc2D.PIE);
        shape.subtract(new Area(arc));

        g.fill(shape);
    }

    private void fillBottomRightQuarterMinusArc(Graphics2D g, int x, int y, int moduleSize, int arcSize) {
        Area shape = new Area(new Rectangle2D.Double(
                x + moduleSize / 2.0 + 1, y + moduleSize / 2.0 + 1, moduleSize / 2.0 - 1, moduleSize / 2.0 - 1));

        Arc2D arc = new Arc2D.Double(
                x + moduleSize - arcSize * 2.0,
                y + moduleSize - arcSize * 2.0,
                arcSize * 2.0,
                arcSize * 2.0,
                270,
                90,
                Arc2D.PIE);
        shape.subtract(new Area(arc));

        g.fill(shape);
    }

    private void drawFinderPatternRoundedStyle(Graphics2D graphics, int x, int y, int squareSize) {
        final int WHITE_RECTANGLE_SIZE = squareSize * 5 / 7;
        final int WHITE_RECTANGLE_OFFSET = squareSize / 7;
        final int MIDDLE_DOT_SIZE = squareSize * 3 / 7;
        final int MIDDLE_DOT_OFFSET = squareSize * 2 / 7;

        final int OUTER_CORNER_RADIUS = squareSize / 2; // Larger radius for outer rectangle
        final int INNER_CORNER_RADIUS = squareSize / 4; // Smaller radius for inner rectangles

        // Debug: Clear the entire area of the finder pattern
        graphics.setComposite(AlphaComposite.Src);
        graphics.setBackground(BACKGROUND);
        graphics.clearRect(x, y, squareSize, squareSize);

        // Outer black rectangle
        graphics.setColor(COLOR);
        graphics.fillRoundRect(x, y, squareSize, squareSize, OUTER_CORNER_RADIUS, OUTER_CORNER_RADIUS);

        // Inner white rectangle
        graphics.setColor(BACKGROUND);
        graphics.fillRoundRect(
                x + WHITE_RECTANGLE_OFFSET,
                y + WHITE_RECTANGLE_OFFSET,
                WHITE_RECTANGLE_SIZE,
                WHITE_RECTANGLE_SIZE,
                INNER_CORNER_RADIUS,
                INNER_CORNER_RADIUS);

        // Center black rectangle
        graphics.setColor(COLOR);
        graphics.fillRoundRect(
                x + MIDDLE_DOT_OFFSET,
                y + MIDDLE_DOT_OFFSET,
                MIDDLE_DOT_SIZE,
                MIDDLE_DOT_SIZE,
                INNER_CORNER_RADIUS,
                INNER_CORNER_RADIUS);
    }
}
