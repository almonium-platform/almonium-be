package com.almonium.infra.chat.service;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.config.properties.AppProperties;
import com.almonium.user.core.exception.StreamIntegrationException;
import io.getstream.chat.java.exceptions.StreamException;
import io.getstream.chat.java.models.Channel;
import io.getstream.chat.java.models.DeleteStrategy;
import io.getstream.chat.java.models.Sort;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

/**
 * Empties the Stream application and rebuilds the channels the app expects to find. Stream holds
 * state we cannot reach with a database drop, so a reset of one without the other leaves the two
 * disagreeing; this is the other half.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class StreamPurgeService {
    // Stream caps a query-channels page at 30 and a delete-channels call at 100 cids.
    private static final int PAGE_SIZE = 30;
    private static final int MAX_OFFSET = 1000;
    private static final int DELETE_BATCH_SIZE = 100;

    // Every type we have ever put a channel in, legacy `messaging` DMs included.
    private static final List<String> OUR_CHANNEL_TYPES = List.of("broadcast", "private", "self", "messaging");

    StreamChatService streamChatService;
    StreamUserDirectory userDirectory;
    AppProperties appProperties;
    Environment environment;

    /**
     * What an operator has to type to mean it. Naming the environment keeps the phrase that clears a
     * scratch app from also clearing the real one.
     */
    public String confirmationPhrase() {
        String profile = environment.getActiveProfiles().length == 0
                ? "default"
                : environment.getActiveProfiles()[0];

        return String.format("%s-%s", appProperties.getName().toLowerCase(), profile);
    }

    /**
     * Deletes every channel and every user, then recreates the broadcast channels, leaving Stream as
     * a fresh install finds it. Users and their self chats come back on their next sign-in.
     */
    public PurgeSummary purgeEverything() {
        List<String> cids = listAllCids();
        deleteChannels(cids);

        // The app's own account is not a user; it owns the broadcast channels and has no row to
        // reconcile against, which is why the orphan pass skips it too. Deleting it would also start
        // an asynchronous sweep of everything it created - including the channels rebuilt below.
        String systemUserId = appProperties.getName().toLowerCase();
        List<String> userIds = userDirectory.listAllIds().stream()
                .filter(id -> !systemUserId.equals(id))
                .toList();
        userDirectory.deleteAll(userIds);

        streamChatService.createDefaultChannel();
        streamChatService.createChannelsForSpecificLanguages();

        log.warn("Purged the Stream application: {} channels, {} users", cids.size(), userIds.size());
        return new PurgeSummary(cids.size(), userIds.size());
    }

    private List<String> listAllCids() {
        List<String> cids = new ArrayList<>();

        for (int offset = 0; offset <= MAX_OFFSET; offset += PAGE_SIZE) {
            List<Channel> page = listPage(offset);
            page.stream().map(Channel::getCId).filter(Objects::nonNull).forEach(cids::add);

            if (page.size() < PAGE_SIZE) {
                return cids;
            }
        }

        log.warn("Stopped at Stream's paging limit of {} channels; re-run once this batch is gone", MAX_OFFSET);
        return cids;
    }

    private List<Channel> listPage(int offset) {
        try {
            List<Channel.ChannelGetResponse> channels = Channel.list()
                    .filterCondition("type", Map.of("$in", OUR_CHANNEL_TYPES))
                    .sort(Sort.builder()
                            .field("created_at")
                            .direction(Sort.Direction.ASC)
                            .build())
                    .limit(PAGE_SIZE)
                    .offset(offset)
                    .request()
                    .getChannels();

            return channels == null
                    ? List.of()
                    : channels.stream()
                            .map(Channel.ChannelGetResponse::getChannel)
                            .filter(Objects::nonNull)
                            .toList();
        } catch (StreamException e) {
            throw new StreamIntegrationException(
                    String.format("Error while listing Stream channels at offset %d: %s", offset, e.getMessage()), e);
        }
    }

    private void deleteChannels(List<String> cids) {
        for (int from = 0; from < cids.size(); from += DELETE_BATCH_SIZE) {
            List<String> batch = cids.subList(from, Math.min(from + DELETE_BATCH_SIZE, cids.size()));
            try {
                Channel.deleteMany(batch).setDeleteStrategy(DeleteStrategy.HARD).request();
            } catch (StreamException e) {
                throw new StreamIntegrationException(
                        String.format("Error while deleting %d Stream channels: %s", batch.size(), e.getMessage()), e);
            }
        }
    }

    public record PurgeSummary(int channelsDeleted, int usersDeleted) {}
}
