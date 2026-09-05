package com.almonium.learning.almo.service;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.config.properties.AlmoProperties;
import com.almonium.config.properties.AppProperties;
import com.almonium.user.core.exception.StreamIntegrationException;
import io.getstream.chat.java.exceptions.StreamException;
import io.getstream.chat.java.models.Channel;
import io.getstream.chat.java.models.Event;
import io.getstream.chat.java.models.Message;
import io.getstream.chat.java.models.MessagePaginationParameters;
import io.getstream.chat.java.models.User;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Almo's side of Stream. He is a channel like any other: the same private type the DMs use, with him as the second
 * member, so every client rule about bubbles, receipts and typing applies without a case for him. Only the id shape
 * says whose channel it is, which is what the clients read to pin it and to drop the presence line.
 */
@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class AlmoStreamGateway {
    static final String CHANNEL_TYPE = "private";
    static final String CHANNEL_ID_TEMPLATE = "almo_%s_%s";
    static final String ALMO_NAME = "Almo";
    // The rest pose, served by the web client like every other chat asset.
    static final String AVATAR_PATH = "/chat/almo.png";

    AlmoProperties almoProperties;
    AppProperties appProperties;

    public String almoUserId() {
        return almoProperties.getUserId();
    }

    /** {@code almo_<user>_<lang>}: one channel per learner per target language, addressable before it exists. */
    public String channelId(UUID userId, Language language) {
        return String.format(CHANNEL_ID_TEMPLATE, userId, language.name().toLowerCase(Locale.ROOT));
    }

    public String cid(UUID userId, Language language) {
        return CHANNEL_TYPE + ":" + channelId(userId, language);
    }

    /** Idempotent, and run before every ensure: a purged Stream application must not leave Almo without an account. */
    public void ensureAlmoUser() {
        try {
            User.upsert()
                    .user(User.UserRequestObject.builder()
                            .id(almoUserId())
                            .name(ALMO_NAME)
                            .additionalField("image", appProperties.getWebDomain() + AVATAR_PATH)
                            .build())
                    .request();
        } catch (StreamException e) {
            throw new StreamIntegrationException("Error while creating Almo's Stream user: " + e.getMessage(), e);
        }
    }

    /**
     * The channel for one language. Get-or-create, so the clients can ask for it on every load; the name and the
     * language ride on the channel because the clients have nothing else to draw the row from.
     */
    public String ensureChannel(UUID userId, Language language, String name) {
        String channelId = channelId(userId, language);
        try {
            Channel.getOrCreate(CHANNEL_TYPE, channelId)
                    .data(Channel.ChannelRequestObject.builder()
                            .createdBy(User.UserRequestObject.builder()
                                    .id(almoUserId())
                                    .build())
                            .members(List.of(member(userId.toString()), member(almoUserId())))
                            .additionalField("name", name)
                            .additionalField("almo", true)
                            .additionalField("language", language.name())
                            .build())
                    .request();
            return CHANNEL_TYPE + ":" + channelId;
        } catch (StreamException e) {
            throw new StreamIntegrationException(
                    String.format("Error while creating Almo channel %s: %s", channelId, e.getMessage()), e);
        }
    }

    /** The tail of the thread, oldest first, as Stream returns it. */
    public List<Message> recentMessages(String channelId, int limit) {
        try {
            return Channel.getOrCreate(CHANNEL_TYPE, channelId)
                    .messages(MessagePaginationParameters.builder().limit(limit).build())
                    .request()
                    .getMessages();
        } catch (StreamException e) {
            throw new StreamIntegrationException(
                    String.format("Error while reading Almo channel %s: %s", channelId, e.getMessage()), e);
        }
    }

    /** The standard three-dot indicator, because he types the way a contact types. Best effort: a miss is not a failure. */
    public void typing(String channelId, boolean started) {
        try {
            Event.send(CHANNEL_TYPE, channelId)
                    .event(Event.EventRequestObject.builder()
                            .type(started ? "typing.start" : "typing.stop")
                            .userId(almoUserId())
                            .build())
                    .request();
        } catch (StreamException e) {
            log.warn("Could not send Almo's typing event in {}: {}", channelId, e.getMessage());
        }
    }

    public String sendReply(String channelId, String text, Map<String, Object> fields) {
        try {
            return Message.send(CHANNEL_TYPE, channelId)
                    .message(Message.MessageRequestObject.builder()
                            .text(text)
                            .userId(almoUserId())
                            .additionalFields(fields)
                            .build())
                    .request()
                    .getMessage()
                    .getId();
        } catch (StreamException e) {
            throw new StreamIntegrationException(
                    String.format("Error while sending Almo's reply in %s: %s", channelId, e.getMessage()), e);
        }
    }

    /**
     * Marks the learner's own message with the queue words it used, so their bubble carries the same dotted line his
     * does. Written as the learner, since it is their message; best effort, because the reply already went out.
     */
    public void annotateMessage(String messageId, UUID authorId, Map<String, Object> fields) {
        try {
            Message.partialUpdate(messageId)
                    .userId(authorId.toString())
                    .setValues(fields)
                    .request();
        } catch (StreamException e) {
            log.warn("Could not annotate message {} with Almo's words: {}", messageId, e.getMessage());
        }
    }

    private Channel.ChannelMemberRequestObject member(String userId) {
        return Channel.ChannelMemberRequestObject.builder().userId(userId).build();
    }
}
