package com.almonium.infra.email.service;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.config.properties.AppProperties;
import com.almonium.infra.email.dto.EmailDto;
import com.almonium.infra.email.exception.EmailConfigurationException;
import com.almonium.util.HtmlFileWriter;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class EmailService {
    RestTemplate restTemplate = new RestTemplate();
    HtmlFileWriter htmlFileWriter;
    AppProperties appProperties;

    public void sendEmail(EmailDto emailDto) {
        if (appProperties.getEmail().isDryRun()) {
            log.info("Email sending is disabled. Skipping sending email to {}", emailDto.recipient());
            htmlFileWriter.saveEmailToFile(emailDto);
            return;
        }

        try {
            AppProperties.Email emailProps = appProperties.getEmail();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(List.of(MediaType.APPLICATION_JSON));
            headers.set("Authorization", "Zoho-enczapikey " + emailProps.getApiKey());

            HttpEntity<Map<String, Object>> request = getMapHttpEntity(emailDto, emailProps, headers);
            ResponseEntity<String> response =
                    restTemplate.exchange(emailProps.getApiUrl(), HttpMethod.POST, request, String.class);

            log.debug("ZeptoMail response: {}", response.getBody());
        } catch (RestClientException e) {
            log.error("Failed to send email to {}: {}", emailDto.recipient(), e.getMessage());
            throw new EmailConfigurationException("Failed to send email: " + e.getMessage(), e);
        }
    }

    private @NonNull HttpEntity<Map<String, Object>> getMapHttpEntity(EmailDto emailDto, AppProperties.Email emailProps, HttpHeaders headers) {
        Map<String, Object> body = Map.of(
                "from",
                        Map.of(
                                "address", emailProps.getFromAddress(),
                                "name", emailProps.getFromName()),
                "to", List.of(Map.of("email_address", Map.of("address", emailDto.recipient()))),
                "subject", emailDto.subject(),
                "htmlbody", emailDto.body());

        return new HttpEntity<>(body, headers);
    }
}
