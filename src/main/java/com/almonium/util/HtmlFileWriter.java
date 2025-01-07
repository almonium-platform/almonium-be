package com.almonium.util;

import jakarta.mail.BodyPart;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HtmlFileWriter {
    private static final String TEMP_EMAIL_PATH = "temp/rendered_email.html";

    @SneakyThrows
    public void saveMimeMessageToFile(MimeMessage mimeMessage) {
        // Force the MimeMessage to be fully written out to trigger any lazy processing
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        mimeMessage.writeTo(outputStream);

        // Now parse and save the content
        String content = parseMultipart((MimeMultipart) mimeMessage.getContent());
        Files.write(Paths.get(TEMP_EMAIL_PATH), content.getBytes());
    }

    @SneakyThrows
    public String parseMultipart(MimeMultipart mimeMultipart) {
        StringBuilder htmlContent = new StringBuilder();

        for (int i = 0; i < mimeMultipart.getCount(); i++) {
            BodyPart bodyPart = mimeMultipart.getBodyPart(i);

            if (bodyPart.isMimeType("multipart/*")) {
                // Recursive call to handle nested multipart
                MimeMultipart nestedMultipart = (MimeMultipart) bodyPart.getContent();
                htmlContent.append(parseMultipart(nestedMultipart));
            } else if (bodyPart.isMimeType(MediaType.TEXT_HTML_VALUE)) {
                htmlContent.append((String) bodyPart.getContent());
            } else if (bodyPart.isMimeType(MediaType.IMAGE_PNG_VALUE)
                    || bodyPart.isMimeType(MediaType.IMAGE_JPEG_VALUE)
                    || bodyPart.isMimeType("image/svg+xml")) {
                String contentId = bodyPart.getHeader("Content-ID")[0];
                if (contentId != null) {
                    contentId = contentId.replaceAll("[<>]", ""); // Remove angle brackets from content ID
                    String base64Image = encodeImageToBase64((MimeBodyPart) bodyPart);
                    String base64Tag = "data:" + bodyPart.getContentType() + ";base64," + base64Image;
                    String imageTag = "cid:" + contentId;
                    int startIndex = htmlContent.indexOf(imageTag);
                    if (startIndex != -1) {
                        htmlContent.replace(startIndex, startIndex + imageTag.length(), base64Tag);
                    } else {
                        // If not found, append the image at the end of HTML
                        htmlContent.append("<img src=\"").append(base64Tag).append("\" alt=\"Embedded Image\"/>");
                    }
                }
            }
        }

        return htmlContent.toString();
    }

    private String encodeImageToBase64(MimeBodyPart bodyPart) throws IOException, MessagingException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bodyPart.getDataHandler().writeTo(outputStream);
        byte[] imageBytes = outputStream.toByteArray();
        return java.util.Base64.getEncoder().encodeToString(imageBytes);
    }
}
