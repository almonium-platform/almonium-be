package com.almonium.infra.chat.service;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.config.properties.AppProperties;
import com.almonium.user.core.exception.StreamIntegrationException;
import com.almonium.user.core.model.entity.User;
import com.almonium.user.core.repository.LearnerRepository;
import com.almonium.user.core.repository.UserRepository;
import io.getstream.chat.java.exceptions.StreamException;
import io.getstream.chat.java.models.Channel;
import io.getstream.chat.java.models.DeleteStrategy;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
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

    // The types this app creates channels in. `messaging` held the DMs before they moved to their
    // own type; those are gone, and querying that type times out on this application even when it
    // holds nothing at all, so the sweep does not ask about it. Anything left there is legacy and
    // deleted by hand.
    private static final List<String> OUR_CHANNEL_TYPES = List.of("broadcast", "private", "self");

    StreamChatService streamChatService;
    StreamUserDirectory userDirectory;
    UserRepository userRepository;
    LearnerRepository learnerRepository;
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
     * Deletes every channel and every user, then rebuilds what the database says should be there:
     * the broadcast rooms, and an account with its own Saved Messages for everyone we still have a
     * row for. Signing in does not rebuild any of that - provisioning only runs for a user we have
     * never seen - so a purge that skipped this step would leave every existing account staring at
     * a chat that cannot load.
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

        int restored = provisionKnownUsers();

        log.warn(
                "Purged the Stream application: {} channels, {} users deleted, {} users restored",
                cids.size(),
                userIds.size(),
                restored);
        return new PurgeSummary(cids.size(), userIds.size(), restored);
    }

    /**
     * Gives every account we hold a row for its Stream user, its broadcast memberships and its own
     * Saved Messages. Every step is idempotent, so this repairs a half-provisioned account without
     * touching a healthy one - and registration can leave accounts that way, since Stream setup
     * runs on an event after the row is already committed.
     */
    public int provisionKnownUsers() {
        List<User> users = userRepository.findAll();

        users.forEach(user -> {
            streamChatService.setupNewUser(user);
            streamChatService.joinLanguageSpecificChannelsIfAvailable(
                    user, learnerRepository.findAllLanguagesByUserId(user.getId()));
        });

        return users.size();
    }

    // One type at a time, on Stream's own default ordering. A `$in` across types, or a sort Stream
    // does not index, turns this into a scan that times out before it returns a single page.
    private List<String> listAllCids() {
        Set<String> cids = new LinkedHashSet<>();
        OUR_CHANNEL_TYPES.forEach(type -> cids.addAll(listCidsOfType(type)));
        return List.copyOf(cids);
    }

    private List<String> listCidsOfType(String type) {
        List<String> cids = new ArrayList<>();

        for (int offset = 0; offset <= MAX_OFFSET; offset += PAGE_SIZE) {
            List<Channel> page = listPage(type, offset);
            page.stream().map(Channel::getCId).filter(Objects::nonNull).forEach(cids::add);

            if (page.size() < PAGE_SIZE) {
                return cids;
            }
        }

        log.warn(
                "Stopped at Stream's paging limit of {} {} channels; re-run once this batch is gone", MAX_OFFSET, type);
        return cids;
    }

    private List<Channel> listPage(String type, int offset) {
        try {
            List<Channel.ChannelGetResponse> channels = Channel.list()
                    .filterCondition("type", type)
                    // A channel query hauls each channel's recent messages and members back with it
                    // by default. All we want is the cid, and asking for the rest is what makes this
                    // time out on a type with any history behind it.
                    .messageLimit(0)
                    .memberLimit(0)
                    .state(false)
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
                    String.format("Error while listing %s channels at offset %d: %s", type, offset, e.getMessage()), e);
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

    public record PurgeSummary(int channelsDeleted, int usersDeleted, int usersRestored) {}
}
