package com.almonium.learning.cover.client;

import com.almonium.analyzer.client.exception.ApiIntegrationException;
import com.almonium.config.properties.CoverProperties;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

/**
 * The eye: a vision model is shown the candidates that already passed the licence rule and picks the one that works
 * as a cover, or none. It judges the picture only. Whether a file is free to use is never asked of it.
 */
@Component
@RequiredArgsConstructor
public class CoverJudge {
    private static final String INSTRUCTIONS = """
        You choose cover art for a classic book in a reading app. You are shown numbered candidate images, all \
        already cleared for copyright. Pick the one that works best as the book's cover, or none.

        A good cover is an illustration, engraving, drawing or painting that shows a scene, a figure or a place \
        from this book or made for it, and reads clearly when printed small in one ink. It is a flat scan of the \
        artwork.

        Never pick: a title page or any page that is mostly text; a portrait or photograph of the author; a \
        photograph of a physical book, a building, a statue or a museum room; a map, diagram or family tree; a \
        film still or anything modern; an image that has nothing to do with this book; a scan too dark, faint or \
        damaged to read.

        If no candidate is good, pick none: a plain typographic cover is better than a wrong picture.

        Answer with one JSON object and nothing else:
        {"pick": <the number of the image, or 0 for none>, "focus_x": <0-100>, "focus_y": <0-100>, \
        "reason": "<one short sentence>"}
        focus_x and focus_y give the centre of the main subject as a percentage from the left and from the top; \
        the cover shows a square crop around it.
        """;

    /** Room for the model to think before it answers; the verdict itself is a line. */
    private static final int MAX_TOKENS = 3000;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final CoverProperties properties;

    /** One image the model is shown, with the file name it has on Commons, which often says what it depicts. */
    public record Shown(String title, String mime, byte[] bytes) {}

    /** {@code pick} is the index into the list shown, or -1 for none. */
    public record Verdict(int pick, int focusX, int focusY, String reason, String model) {}

    public Verdict choose(String bookTitle, String author, List<Shown> shown) {
        List<Map<String, Object>> content = new ArrayList<>();
        content.add(text("The book: \"" + bookTitle + "\" by " + author + ". " + shown.size() + " candidates follow."));
        for (int index = 0; index < shown.size(); index++) {
            Shown image = shown.get(index);
            content.add(text("Image " + (index + 1) + ", file name: " + image.title()));
            content.add(Map.of(
                    "type",
                    "image_url",
                    "image_url",
                    Map.of(
                            "url",
                            "data:" + image.mime() + ";base64,"
                                    + Base64.getEncoder().encodeToString(image.bytes()))));
        }
        Map<String, Object> body = Map.of(
                "model", properties.getModel(),
                "max_tokens", MAX_TOKENS,
                "messages",
                        List.of(
                                Map.of("role", "system", "content", INSTRUCTIONS),
                                Map.of("role", "user", "content", content)));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(properties.getApiKey());
        try {
            JsonNode response =
                    restTemplate.postForObject(properties.getUrl(), new HttpEntity<>(body, headers), JsonNode.class);
            String answer = response == null
                    ? ""
                    : response.at("/choices/0/message/content").asString("");
            return read(
                    answer,
                    shown.size(),
                    response == null
                            ? properties.getModel()
                            : response.path("model").asString(properties.getModel()));
        } catch (RestClientException e) {
            throw new ApiIntegrationException("The cover model could not be reached: " + e.getMessage(), e);
        }
    }

    Verdict read(String answer, int shown, String model) {
        int open = answer.indexOf('{');
        int close = answer.lastIndexOf('}');
        if (open < 0 || close < open) {
            throw new ApiIntegrationException("The cover model answered with something other than a verdict: "
                    + (answer.length() > 200 ? answer.substring(0, 200) : answer));
        }
        try {
            JsonNode verdict = objectMapper.readTree(answer.substring(open, close + 1));
            int pick = verdict.path("pick").asInt(0);
            return new Verdict(
                    pick >= 1 && pick <= shown ? pick - 1 : -1,
                    percent(verdict.path("focus_x").asInt(50)),
                    percent(verdict.path("focus_y").asInt(50)),
                    verdict.path("reason").asString(""),
                    model);
        } catch (JacksonException e) {
            throw new ApiIntegrationException("The cover model answered with something other than a verdict", e);
        }
    }

    private static int percent(int value) {
        return Math.max(0, Math.min(100, value));
    }

    private static Map<String, Object> text(String text) {
        return Map.of("type", "text", "text", text);
    }
}
