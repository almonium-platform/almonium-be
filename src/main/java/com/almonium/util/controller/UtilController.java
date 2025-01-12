package com.almonium.util.controller;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.infra.qr.service.QRCodeGenerator;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/utils")
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class UtilController {
    QRCodeGenerator qrCodeGenerator;

    @GetMapping("/qr")
    public ResponseEntity<byte[]> getQRCode(@RequestParam String text) {
        byte[] qrCodeImageBytes = qrCodeGenerator.generateQRCode(text);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"qrcode.png\"")
                .contentType(MediaType.IMAGE_PNG)
                .body(qrCodeImageBytes);
    }
}
