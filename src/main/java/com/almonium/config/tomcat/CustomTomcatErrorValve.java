package com.almonium.config.tomcat;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.Response;
import org.apache.catalina.valves.ErrorReportValve;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@Slf4j
public class CustomTomcatErrorValve extends ErrorReportValve {

    @Override
    protected void report(Request request, Response response, Throwable throwable) {
        if (!response.setErrorReported()) {
            return;
        }

        int statusCode = response.getStatus();
        String reasonPhrase = HttpStatus.valueOf(statusCode).getReasonPhrase();

        try {
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            String jsonResponse = String.format("""
                    {
                        "title": "%s",
                        "status": %d
                    }
                    """, reasonPhrase, statusCode);
            response.getWriter().write(jsonResponse);
            response.getWriter().flush();

            log.error(
                    String.format("Error reported: %d %s - URI: %s", statusCode, reasonPhrase, request.getRequestURI()),
                    throwable);
        } catch (IOException e) {
            log.error("Failed to write custom error response", e);
        }
    }
}
