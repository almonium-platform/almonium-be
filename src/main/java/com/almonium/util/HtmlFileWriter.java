package com.almonium.util;

import com.almonium.infra.email.dto.EmailDto;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HtmlFileWriter {
    private static final String TEMP_EMAIL_PATH = "temp/rendered_email.html";

    @SneakyThrows
    public void saveEmailToFile(EmailDto emailDto) {
        String content = emailDto.body();
        // create temp directory if not exists
        Path path = Paths.get(TEMP_EMAIL_PATH);
        Files.createDirectories(path.getParent());
        Files.writeString(path, content);
    }
}
