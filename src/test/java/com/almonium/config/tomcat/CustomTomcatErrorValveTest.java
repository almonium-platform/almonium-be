package com.almonium.config.tomcat;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.PrintWriter;
import java.io.StringWriter;
import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;

@ExtendWith(OutputCaptureExtension.class)
class CustomTomcatErrorValveTest {
    private final CustomTomcatErrorValve valve = new CustomTomcatErrorValve();

    @Test
    void scannerNotFoundIsAnsweredWithoutAnErrorLog(CapturedOutput output) throws Exception {
        StringWriter body = report(404, "/.git/config");

        assertThat(body.toString()).contains("\"message\": \"Not Found\"");
        assertThat(output).doesNotContain("Error reported");
    }

    @Test
    void serverFailureIsLoggedAsAnError(CapturedOutput output) throws Exception {
        report(500, "/api/v1/books");

        assertThat(output).contains("ERROR").contains("Error reported: 500 Internal Server Error - URI: /api/v1/books");
    }

    private StringWriter report(int status, String uri) throws Exception {
        Request request = mock(Request.class);
        Response response = mock(Response.class);
        StringWriter body = new StringWriter();
        when(request.getRequestURI()).thenReturn(uri);
        when(response.setErrorReported()).thenReturn(true);
        when(response.getStatus()).thenReturn(status);
        when(response.getWriter()).thenReturn(new PrintWriter(body));

        valve.report(request, response, null);
        return body;
    }
}
