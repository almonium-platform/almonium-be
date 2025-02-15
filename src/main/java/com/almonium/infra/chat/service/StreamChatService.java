package com.almonium.infra.chat.service;

import static io.getstream.chat.java.models.User.createToken;
import static io.getstream.chat.java.models.User.upsert;
import static lombok.AccessLevel.PRIVATE;

import com.almonium.user.core.exception.StreamIntegrationException;
import com.almonium.user.core.model.entity.User;
import io.getstream.chat.java.exceptions.StreamException;
import io.getstream.chat.java.models.Channel;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class StreamChatService {
    private static final String SELF_CHAT_AVATAR_URL =
            "https://firebasestorage.googleapis.com/v0/b/almonium.firebasestorage.app/o/other%2Fsaved.png?alt=media&token=cfd7709d-b73a-4441-b106-334878059a34";
    private static final String SELF_CHAT_NAME = "Saved Messages";
    private static final String SELF_CHAT_TYPE = "messaging";
    private static final String SELF_CHAT_ID_TEMPLATE = "self_%s";

    public String setStreamChatToken(User user) {
        try {
            upsert().user(io.getstream.chat.java.models.User.UserRequestObject.builder()
                            .id(user.getId().toString())
                            .name(user.getUsername())
                            .additionalField("email", user.getEmail())
                            .build())
                    .request();

            return createToken(String.valueOf(user.getId()), null, null);
        } catch (StreamException e) {
            throw new StreamIntegrationException(
                    "Error while setting Stream Chat token for user with id: " + user.getId(), e);
        }
    }

    public void createSelfChat(User user) {
        try {
            String selfChatId = String.format(SELF_CHAT_ID_TEMPLATE, user.getId());

            Channel.ChannelMemberRequestObject selfMember = Channel.ChannelMemberRequestObject.builder()
                    .userId(user.getId().toString())
                    .build();

            Channel.getOrCreate(SELF_CHAT_TYPE, selfChatId)
                    .data(Channel.ChannelRequestObject.builder()
                            .createdBy(io.getstream.chat.java.models.User.UserRequestObject.builder()
                                    .id(user.getId().toString())
                                    .name(user.getUsername())
                                    .build())
                            .members(Collections.singletonList(selfMember))
                            .additionalField("name", SELF_CHAT_NAME)
                            .additionalField("image", SELF_CHAT_AVATAR_URL)
                            .build())
                    .request();

        } catch (StreamException e) {
            throw new StreamIntegrationException("Error while creating self-chat for user with id: " + user.getId(), e);
        }
    }
}
