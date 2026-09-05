package com.almonium.infra.chat.service;

import static io.getstream.chat.java.models.User.createToken;
import static io.getstream.chat.java.models.User.upsert;
import static lombok.AccessLevel.PRIVATE;

import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.config.properties.AppProperties;
import com.almonium.infra.chat.dto.request.AnnouncementRequest;
import com.almonium.subscription.service.EffectiveAccessService;
import com.almonium.user.core.exception.StreamIntegrationException;
import com.almonium.user.core.model.entity.User;
import io.getstream.chat.java.exceptions.StreamException;
import io.getstream.chat.java.models.Channel;
import io.getstream.chat.java.models.Message;
import jakarta.persistence.EntityNotFoundException;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class StreamChatService {
    private static final String SELF_CHAT_NAME = "Saved Messages";
    private static final String SELF_CHAT_TYPE = "self";

    private static final String READ_ONLY_CHAT_TYPE = "broadcast";

    private static final String PRIVATE_CHAT_TYPE = "private";

    // A DM is addressed by the friendship it belongs to, so both clients can name the channel before
    // it exists - and a friendship can never end up with two of them.
    private static final String PRIVATE_CHANNEL_ID_TEMPLATE = "private_%s";

    // Membership travels with the user record so the clients can mark a member without asking us
    // who is one. Only the flag: what it entitles someone to is ours to decide, not Stream's.
    private static final String PREMIUM_FIELD = "premium";

    // Channel artwork ships with the web client, like the email assets do; no third-party shortener
    // in front of a URL only Stream ever reads.
    private static final String CHANNEL_IMAGE_TEMPLATE = "%s/chat/%s.png";

    private static final List<Language> SUPPORTED_LANGUAGES =
            List.of(Language.EN, Language.DE, Language.ES, Language.FR, Language.IT);
    AppProperties appProperties;
    EffectiveAccessService effectiveAccessService;

    public String setupNewUser(User user) {
        createStreamUser(user);
        joinDefaultChannels(user);
        createSelfChat(user);
        return generateStreamToken(user);
    }

    public void joinLanguageSpecificChannelsIfAvailable(User user, List<Language> languages) {
        languages.forEach(language -> joinLanguageSpecificChannelIfAvailable(user, language));
    }

    public void joinLanguageSpecificChannelIfAvailable(User user, Language language) {
        if (!SUPPORTED_LANGUAGES.contains(language)) {
            return;
        }

        joinBroadcastChannel(getSupportedLanguageChannelId(language), user, () -> createLanguageChannel(language));
    }

    public void leaveLanguageSpecificChannelIfAvailable(User user, Language language) {
        if (!SUPPORTED_LANGUAGES.contains(language)) {
            return;
        }

        try {
            Channel.update(READ_ONLY_CHAT_TYPE, getSupportedLanguageChannelId(language))
                    .removeMember(String.valueOf(user.getId()))
                    .request();
        } catch (StreamException e) {
            throw new StreamIntegrationException(
                    String.format("Error while joining language specific channel: %s, %s", language, e.getMessage()),
                    e);
        }
    }

    public void joinDefaultChannels(User user) {
        joinBroadcastChannel(getDefaultStreamId(), user, this::createDefaultChannel);
    }

    /**
     * The system channels are created once, by hand, and can simply not be there: a fresh Stream
     * app, a purged one, a project nobody bootstrapped. A signup that dies on that leaves the user
     * with no chat at all - not even their own Saved Messages, which is created afterwards - so a
     * missing channel is rebuilt rather than raised.
     */
    private void joinBroadcastChannel(String channelId, User user, Runnable recreate) {
        try {
            addMember(channelId, user);
        } catch (StreamException first) {
            log.warn("Broadcast channel {} would not take a member, rebuilding it: {}", channelId, first.getMessage());
            recreate.run();
            try {
                addMember(channelId, user);
            } catch (StreamException second) {
                throw new StreamIntegrationException(
                        String.format("Error while joining channel %s: %s", channelId, second.getMessage()), second);
            }
        }
    }

    private void addMember(String channelId, User user) throws StreamException {
        Channel.update(READ_ONLY_CHAT_TYPE, channelId)
                .addMember(String.valueOf(user.getId()))
                .request();
    }

    /**
     * Posts to a broadcast channel as the app itself. Members cannot write to those channels, so
     * this server-side path is the only way anything is published there.
     */
    public String publishAnnouncement(AnnouncementRequest request) {
        String channelId = getBroadcastChannelId(request.language());

        try {
            var message =
                    Message.MessageRequestObject.builder().text(request.text()).userId(getDefaultStreamId());
            // The client draws the footer action from these; a post without them is text alone.
            if (request.hasCta()) {
                message.additionalField("ctaLabel", request.ctaLabel()).additionalField("ctaUrl", request.ctaUrl());
            }
            return Message.send(READ_ONLY_CHAT_TYPE, channelId)
                    .message(message.build())
                    .request()
                    .getMessage()
                    .getId();
        } catch (StreamException e) {
            throw new StreamIntegrationException(
                    String.format("Error while publishing to channel %s: %s", channelId, e.getMessage()), e);
        }
    }

    public void createStreamUser(User user) {
        try {
            upsert().user(io.getstream.chat.java.models.User.UserRequestObject.builder()
                            .id(user.getId().toString())
                            .name(user.getUsername())
                            .additionalField("email", user.getEmail())
                            .additionalField(PREMIUM_FIELD, effectiveAccessService.isPremium(user))
                            .build())
                    .request();
        } catch (StreamException e) {
            throw new StreamIntegrationException(
                    String.format("Error while creating user with id: %s, message: %s", user.getId(), e.getMessage()),
                    e);
        }
    }

    public String generateStreamToken(User user) {
        return createToken(String.valueOf(user.getId()), null, null);
    }

    public void updateUser(User user) {
        try {
            io.getstream.chat.java.models.User.UserRequestObject userRequest =
                    io.getstream.chat.java.models.User.UserRequestObject.builder()
                            .id(String.valueOf(user.getId())) // User ID to update
                            .name(user.getUsername()) // User name
                            .additionalField("email", user.getEmail()) // User email
                            .additionalField("image", user.getProfile().getAvatarUrl()) // New avatar URL
                            .additionalField(PREMIUM_FIELD, effectiveAccessService.isPremium(user))
                            .build();

            // Upsert the user with the new avatar URL
            upsert().user(userRequest).request();

        } catch (StreamException e) {
            throw new StreamIntegrationException(
                    String.format("Error while updating user with id: %s, %s", user.getId(), e.getMessage()), e);
        }
    }

    /**
     * The private chat a friendship owns. Idempotent, so a redelivered event costs nothing; the members arrive with the
     * channel, which is what puts it on both clients' screens.
     */
    public void createPrivateChat(UUID relationshipId, UUID accepterId, UUID counterpartId) {
        String channelId = getPrivateChannelId(relationshipId);

        try {
            Channel.getOrCreate(PRIVATE_CHAT_TYPE, channelId)
                    .data(Channel.ChannelRequestObject.builder()
                            .createdBy(io.getstream.chat.java.models.User.UserRequestObject.builder()
                                    .id(accepterId.toString())
                                    .build())
                            .members(List.of(asMember(accepterId), asMember(counterpartId)))
                            .build())
                    .request();

        } catch (StreamException e) {
            throw new StreamIntegrationException(
                    String.format("Error while creating private chat %s: %s", channelId, e.getMessage()), e);
        }
    }

    private Channel.ChannelMemberRequestObject asMember(UUID userId) {
        return Channel.ChannelMemberRequestObject.builder()
                .userId(userId.toString())
                .build();
    }

    private String getPrivateChannelId(UUID relationshipId) {
        return String.format(PRIVATE_CHANNEL_ID_TEMPLATE, relationshipId);
    }

    public void createSelfChat(User user) {
        try {
            Channel.ChannelMemberRequestObject selfMember = Channel.ChannelMemberRequestObject.builder()
                    .userId(user.getId().toString())
                    .build();

            Channel.getOrCreate(SELF_CHAT_TYPE, user.getId().toString())
                    .data(Channel.ChannelRequestObject.builder()
                            .createdBy(io.getstream.chat.java.models.User.UserRequestObject.builder()
                                    .id(user.getId().toString())
                                    .name(user.getUsername())
                                    .build())
                            .members(Collections.singletonList(selfMember))
                            .additionalField("name", SELF_CHAT_NAME)
                            .build())
                    .request();

        } catch (StreamException e) {
            throw new StreamIntegrationException(
                    String.format("Error while creating self chat for user: %s, %s", user.getId(), e.getMessage()), e);
        }
    }

    public void cleanUpUserData(UUID userId) {
        String id = String.valueOf(userId);
        cleanUpChannels(id);
        deleteUserFromStream(id);
    }

    private void cleanUpChannels(String userId) {
        SUPPORTED_LANGUAGES.stream()
                .map(this::getSupportedLanguageChannelId)
                .forEach(lang -> leavePublicChannel(lang, userId));

        leavePublicChannel(getDefaultStreamId(), userId);
        deleteSelfChat(userId);
    }

    private void deleteSelfChat(String userId) {
        try {
            Channel.delete(SELF_CHAT_TYPE, userId).request();
        } catch (StreamException e) {
            log.error("Error while deleting self chat for user: {}, {}", userId, e.getMessage());
        }
    }

    private void leavePublicChannel(String channelId, String userId) {
        try {
            Channel.update(READ_ONLY_CHAT_TYPE, channelId).removeMember(userId).request();
        } catch (StreamException e) {
            log.error("Error while removing user from language specific channel: {}, {}", channelId, e.getMessage());
        }
    }

    private void deleteUserFromStream(String userId) {
        try {
            io.getstream.chat.java.models.User.delete(userId).request();
        } catch (StreamException e) {
            throw new StreamIntegrationException(
                    String.format("Error while deleting user %s from Stream: %s", userId, e.getMessage()), e);
        }
    }

    private String getChannelImage(String key) {
        return String.format(CHANNEL_IMAGE_TEMPLATE, appProperties.getWebDomain(), key);
    }

    // No language means the app-wide channel; anything else has to be a room we actually run.
    private String getBroadcastChannelId(Language language) {
        if (language == null) {
            return getDefaultStreamId();
        }

        if (!SUPPORTED_LANGUAGES.contains(language)) {
            throw new EntityNotFoundException("No broadcast channel for language: " + language);
        }

        return getSupportedLanguageChannelId(language);
    }

    // both default channel and default user id are based on the app name
    private String getDefaultStreamId() {
        return appProperties.getName().toLowerCase();
    }

    // Re-stamps the artwork on the system channels; run after the asset URLs move. Safe to re-run.
    public void syncSystemChannelImages() {
        try {
            Channel.partialUpdate(READ_ONLY_CHAT_TYPE, getDefaultStreamId())
                    .setValue("image", getChannelImage("logo"))
                    .request();

            for (var language : SUPPORTED_LANGUAGES) {
                Channel.partialUpdate(READ_ONLY_CHAT_TYPE, getSupportedLanguageChannelId(language))
                        .setValue("image", getLogoForLanguageChannel(language))
                        .request();
            }
        } catch (StreamException e) {
            throw new StreamIntegrationException("Error while syncing system channel images: " + e.getMessage(), e);
        }
    }

    public void createDefaultChannel() { // Fetch the channel details
        try {
            String defaultChannelId = getDefaultStreamId();

            Channel.getOrCreate(READ_ONLY_CHAT_TYPE, defaultChannelId)
                    .data(Channel.ChannelRequestObject.builder()
                            .createdBy(io.getstream.chat.java.models.User.UserRequestObject.builder()
                                    .id(defaultChannelId)
                                    .build())
                            .additionalField("image", getChannelImage("logo"))
                            .additionalField("name", appProperties.getName())
                            .build())
                    .request();
        } catch (StreamException e) {
            throw new StreamIntegrationException("Error while creating default channel: " + e.getMessage(), e);
        }
    }

    public void createChannelsForSpecificLanguages() {
        SUPPORTED_LANGUAGES.forEach(this::createLanguageChannel);
    }

    public void createLanguageChannel(Language language) {
        try {
            Channel.getOrCreate(READ_ONLY_CHAT_TYPE, getSupportedLanguageChannelId(language))
                    .data(Channel.ChannelRequestObject.builder()
                            .createdBy(io.getstream.chat.java.models.User.UserRequestObject.builder()
                                    .id(getDefaultStreamId())
                                    .build())
                            .additionalField("image", getLogoForLanguageChannel(language))
                            .additionalField("name", getLanguageChannelName(language))
                            .build())
                    .request();
        } catch (StreamException e) {
            throw new StreamIntegrationException(
                    String.format("Error while creating the %s channel: %s", language, e.getMessage()), e);
        }
    }

    private String getLanguageChannelName(Language language) {
        Locale locale = new Locale(language.name().toLowerCase());
        String selfLanguageName = locale.getDisplayLanguage(locale);
        String capitalizedLangName = selfLanguageName.substring(0, 1).toUpperCase() + selfLanguageName.substring(1);

        return String.format("%s - %s", appProperties.getName(), capitalizedLangName);
    }

    private String getLogoForLanguageChannel(Language language) {
        return getChannelImage("logo-" + language.name().toLowerCase());
    }

    private String getSupportedLanguageChannelId(Language language) {
        return "almonium-" + language.name().toLowerCase();
    }
}
