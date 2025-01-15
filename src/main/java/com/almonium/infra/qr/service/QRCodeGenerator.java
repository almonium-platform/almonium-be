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

    /**
     * Encodes the text into a QRCode object (via ZXing).
     */
    private QRCode generateQRCodeInternal(String text) {
        try {
            var hints = Map.of(EncodeHintType.CHARACTER_SET, StandardCharsets.UTF_8.name());
            return Encoder.encode(text, ErrorCorrectionLevel.H, hints);
        } catch (WriterException e) {
            throw new QRCodeGenerationException("Failed to generate QR code", e);
        }
    }

    /**
     * Converts the given BufferedImage to a PNG byte array.
     */
    private byte[] bufferedImageToBytes(BufferedImage image) {
        try (ByteArrayOutputStream stream = new ByteArrayOutputStream()) {
            ImageIO.write(image, "png", stream);
            return stream.toByteArray();
        } catch (IOException e) {
            throw new QRCodeGenerationException("Failed to convert BufferedImage to byte array", e);
        }
    }

    /**
     * Renders the entire QR code image as a BufferedImage with custom arcs/corners.
     */
    private BufferedImage renderQRImage(QRCode code) {
        // Create a transparent image
        BufferedImage image = new BufferedImage(SIZE, SIZE, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = image.createGraphics();

        // Anti-aliasing for smoother arcs
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setComposite(AlphaComposite.Src);
        graphics.setBackground(BACKGROUND);
        graphics.clearRect(0, 0, SIZE, SIZE);

        ByteMatrix inputMatrix = code.getMatrix();
        if (inputMatrix == null) {
            throw new QRCodeGenerationException("QR code matrix is null");
        }

        int matrixWidth = inputMatrix.getWidth();
        int matrixHeight = inputMatrix.getHeight();

        // Compute module size and padding to center the QR code in the image
        int moduleSize = Math.min(SIZE / matrixWidth, SIZE / matrixHeight);
        int leftPadding = (SIZE - (matrixWidth * moduleSize)) / 2;
        int topPadding = (SIZE - (matrixHeight * moduleSize)) / 2;

        // Step 1: Clear an exclusion zone in the center (circular "hole")
        clearCenterExclusionZone(inputMatrix);

        // Step 2: Draw each cell (module), applying corner arcs where needed
        for (int matrixRow = 0, outputY = topPadding; matrixRow < matrixHeight; matrixRow++, outputY += moduleSize) {
            for (int matrixCol = 0, outputX = leftPadding;
                    matrixCol < matrixWidth;
                    matrixCol++, outputX += moduleSize) {

                int cellValue = inputMatrix.get(matrixCol, matrixRow);
                if (cellValue == 1) {
                    drawCellIfBlack(graphics, inputMatrix, matrixCol, matrixRow, outputX, outputY, moduleSize);
                } else {
                    drawCellIfWhite(graphics, inputMatrix, matrixCol, matrixRow, outputX, outputY, moduleSize);
                }
            }
        }

        // Draw the three Finder Patterns (top-left, top-right, bottom-left)
        int finderPatternSize = moduleSize * 7;
        drawFinderPatternRoundedStyle(graphics, leftPadding, topPadding, finderPatternSize);
        drawFinderPatternRoundedStyle(
                graphics, leftPadding + (matrixWidth - 7) * moduleSize, topPadding, finderPatternSize);
        drawFinderPatternRoundedStyle(
                graphics, leftPadding, topPadding + (matrixHeight - 7) * moduleSize, finderPatternSize);

        return image;
    }

    /**
     * Clears (sets to 0) the modules within a circular EXCLUSION_RADIUS at the center.
     */
    private void clearCenterExclusionZone(ByteMatrix inputMatrix) {
        int width = inputMatrix.getWidth();
        int height = inputMatrix.getHeight();

        // Center position in the matrix
        int centerX = width / 2;
        int centerY = height / 2;

        // Determine bounding box to iterate
        int startX = Math.max(0, centerX - EXCLUSION_RADIUS);
        int endX = Math.min(width - 1, centerX + EXCLUSION_RADIUS);
        int startY = Math.max(0, centerY - EXCLUSION_RADIUS);
        int endY = Math.min(height - 1, centerY + EXCLUSION_RADIUS);

        for (int y = startY; y <= endY; y++) {
            for (int x = startX; x <= endX; x++) {
                double distance = Math.sqrt(Math.pow(x - centerX, 2) + Math.pow(y - centerY, 2));
                if (distance <= EXCLUSION_RADIUS) {
                    // Clear the dot
                    inputMatrix.set(x, y, 0);
                }
            }
        }
    }

    /**
     * Draws a cell that is "1" (black) from the QR matrix, with the corner arcs where needed.
     */
    private void drawCellIfBlack(
            Graphics2D g, ByteMatrix matrix, int matrixCol, int matrixRow, int outputX, int outputY, int moduleSize) {

        // Fill the entire cell in black
        g.setColor(COLOR);
        g.fillRect(outputX, outputY, moduleSize, moduleSize);

        // Determine if neighboring cells are white to decide if corners get arcs
        boolean topWhite = (matrixRow == 0) || (matrix.get(matrixCol, matrixRow - 1) == 0);
        boolean bottomWhite = (matrixRow == matrix.getHeight() - 1) || (matrix.get(matrixCol, matrixRow + 1) == 0);
        boolean leftWhite = (matrixCol == 0) || (matrix.get(matrixCol - 1, matrixRow) == 0);
        boolean rightWhite = (matrixCol == matrix.getWidth() - 1) || (matrix.get(matrixCol + 1, matrixRow) == 0);

        int arcSize = moduleSize / 2;

        // "Knock out" (clear + fillArc) the corners if top/left (etc.) are white
        if (topWhite && leftWhite) {
            g.clearRect(outputX, outputY, arcSize, arcSize);
            g.fillArc(outputX, outputY, arcSize * 2, arcSize * 2, 90, 90);
        }
        if (topWhite && rightWhite) {
            g.clearRect(outputX + moduleSize - arcSize, outputY, arcSize, arcSize);
            g.fillArc(outputX + moduleSize - arcSize * 2, outputY, arcSize * 2, arcSize * 2, 0, 90);
        }
        if (bottomWhite && leftWhite) {
            g.clearRect(outputX, outputY + moduleSize - arcSize, arcSize, arcSize);
            g.fillArc(outputX, outputY + moduleSize - arcSize * 2, arcSize * 2, arcSize * 2, 180, 90);
        }
        if (bottomWhite && rightWhite) {
            g.clearRect(outputX + moduleSize - arcSize, outputY + moduleSize - arcSize, arcSize, arcSize);
            g.fillArc(
                    outputX + moduleSize - arcSize * 2,
                    outputY + moduleSize - arcSize * 2,
                    arcSize * 2,
                    arcSize * 2,
                    270,
                    90);
        }
    }

    /**
     * Draws a cell that is "0" (white) from the QR matrix, optionally adding
     * "quarter" arcs where adjacent cells are black.
     */
    private void drawCellIfWhite(
            Graphics2D g, ByteMatrix matrix, int matrixCol, int matrixRow, int outputX, int outputY, int moduleSize) {

        // If neighbors are black, we fill "quarters minus arcs"
        boolean topBlack = (matrixRow > 0) && (matrix.get(matrixCol, matrixRow - 1) == 1);
        boolean bottomBlack = (matrixRow < matrix.getHeight() - 1) && (matrix.get(matrixCol, matrixRow + 1) == 1);
        boolean leftBlack = (matrixCol > 0) && (matrix.get(matrixCol - 1, matrixRow) == 1);
        boolean rightBlack = (matrixCol < matrix.getWidth() - 1) && (matrix.get(matrixCol + 1, matrixRow) == 1);

        // Diagonal neighbors
        boolean topLeftBlack = (matrixCol > 0 && matrixRow > 0) && (matrix.get(matrixCol - 1, matrixRow - 1) == 1);
        boolean topRightBlack =
                (matrixCol < matrix.getWidth() - 1 && matrixRow > 0) && (matrix.get(matrixCol + 1, matrixRow - 1) == 1);
        boolean bottomLeftBlack = (matrixCol > 0 && matrixRow < matrix.getHeight() - 1)
                && (matrix.get(matrixCol - 1, matrixRow + 1) == 1);
        boolean bottomRightBlack = (matrixCol < matrix.getWidth() - 1 && matrixRow < matrix.getHeight() - 1)
                && (matrix.get(matrixCol + 1, matrixRow + 1) == 1);

        int arcSize = moduleSize / 2;

        // Fill top-left quarter minus arc if left/top cells are black, plus diagonal
        if (leftBlack && topBlack && topLeftBlack) {
            fillTopLeftCornerQuarter(g, outputX, outputY, moduleSize, arcSize);
        }
        // Fill top-right quarter minus arc if right/top cells are black, plus diagonal
        if (rightBlack && topBlack && topRightBlack) {
            fillTopRightCornerQuarter(g, outputX, outputY, moduleSize, arcSize);
        }
        // Fill bottom-left quarter minus arc if left/bottom cells are black, plus diagonal
        if (leftBlack && bottomBlack && bottomLeftBlack) {
            fillBottomLeftCornerQuarter(g, outputX, outputY, moduleSize, arcSize);
        }
        // Fill bottom-right quarter minus arc if right/bottom cells are black, plus diagonal
        if (rightBlack && bottomBlack && bottomRightBlack) {
            fillBottomRightCornerQuarter(g, outputX, outputY, moduleSize, arcSize);
        }
    }

    /**
     * Fills only the top-left quarter of a cell, minus a rounded arc in the corner.
     */
    private void fillTopLeftCornerQuarter(Graphics2D g, int x, int y, int moduleSize, int arcSize) {
        // Create shape for the top-left quarter only
        Area shape = new Area(new Rectangle2D.Double(x, y, moduleSize / 2.0 - 1, moduleSize / 2.0 - 1));

        // Subtract the arc from that quarter
        Arc2D arc = new Arc2D.Double(x, y, arcSize * 2.0, arcSize * 2.0, 90, 90, Arc2D.PIE);
        shape.subtract(new Area(arc));
        g.fill(shape);
    }

    /**
     * Fills only the top-right quarter of a cell, minus a rounded arc.
     */
    private void fillTopRightCornerQuarter(Graphics2D g, int x, int y, int moduleSize, int arcSize) {
        Area shape = new Area(
                new Rectangle2D.Double(x + moduleSize / 2.0 + 1, y, moduleSize / 2.0 - 1, moduleSize / 2.0 - 1));

        Arc2D arc = new Arc2D.Double(x + moduleSize - arcSize * 2.0, y, arcSize * 2.0, arcSize * 2.0, 0, 90, Arc2D.PIE);
        shape.subtract(new Area(arc));
        g.fill(shape);
    }

    /**
     * Fills only the bottom-left quarter of a cell, minus a rounded arc.
     */
    private void fillBottomLeftCornerQuarter(Graphics2D g, int x, int y, int moduleSize, int arcSize) {
        Area shape = new Area(
                new Rectangle2D.Double(x, y + moduleSize / 2.0 + 1, moduleSize / 2.0 - 1, moduleSize / 2.0 - 1));

        Arc2D arc =
                new Arc2D.Double(x, y + moduleSize - arcSize * 2.0, arcSize * 2.0, arcSize * 2.0, 180, 90, Arc2D.PIE);
        shape.subtract(new Area(arc));
        g.fill(shape);
    }

    /**
     * Fills only the bottom-right quarter of a cell, minus a rounded arc.
     */
    private void fillBottomRightCornerQuarter(Graphics2D g, int x, int y, int moduleSize, int arcSize) {
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

    /**
     * Draws the large Finder Pattern squares with rounded corners.
     */
    private void drawFinderPatternRoundedStyle(Graphics2D g, int x, int y, int squareSize) {
        final int WHITE_RECT_SIZE = squareSize * 5 / 7;
        final int WHITE_RECT_OFFSET = squareSize / 7;
        final int MIDDLE_DOT_SIZE = squareSize * 3 / 7;
        final int MIDDLE_DOT_OFFSET = squareSize * 2 / 7;

        final int OUTER_CORNER_RADIUS = squareSize / 2; // Larger radius for outer rectangle
        final int INNER_CORNER_RADIUS = squareSize / 4; // Smaller radius for inner rectangles

        // Clear the entire area of the finder pattern (in "Src" mode)
        g.setComposite(AlphaComposite.Src);
        g.setBackground(BACKGROUND);
        g.clearRect(x, y, squareSize, squareSize);

        // Outer black rectangle
        g.setColor(COLOR);
        g.fillRoundRect(x, y, squareSize, squareSize, OUTER_CORNER_RADIUS, OUTER_CORNER_RADIUS);

        // Inner white rectangle
        g.setColor(BACKGROUND);
        g.fillRoundRect(
                x + WHITE_RECT_OFFSET,
                y + WHITE_RECT_OFFSET,
                WHITE_RECT_SIZE,
                WHITE_RECT_SIZE,
                INNER_CORNER_RADIUS,
                INNER_CORNER_RADIUS);

        // Center black rectangle
        g.setColor(COLOR);
        g.fillRoundRect(
                x + MIDDLE_DOT_OFFSET,
                y + MIDDLE_DOT_OFFSET,
                MIDDLE_DOT_SIZE,
                MIDDLE_DOT_SIZE,
                INNER_CORNER_RADIUS,
                INNER_CORNER_RADIUS);
    }
}
