package com.almonium.user.core.factory;

import static io.getstream.chat.java.models.User.createToken;
import static io.getstream.chat.java.models.User.upsert;
import static lombok.AccessLevel.PRIVATE;

import com.almonium.subscription.service.PlanSubscriptionService;
import com.almonium.user.core.exception.StreamIntegrationException;
import com.almonium.user.core.model.entity.User;
import com.almonium.user.core.model.enums.SetupStep;
import com.almonium.user.core.repository.UserRepository;
import com.almonium.user.core.service.UsernameGenerator;
import io.getstream.chat.java.exceptions.StreamException;
import java.util.HashSet;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class UserFactory {
    PlanSubscriptionService planSubscriptionService;
    UsernameGenerator usernameGenerator;
    UserRepository userRepository;

    public User createUserWithDefaultPlan(String email, boolean emailVerified) {
        String username = usernameGenerator.generateUsername(email);

        User user = User.builder()
                .email(email)
                .username(username)
                .emailVerified(emailVerified)
                .planSubscriptions(new HashSet<>())
                .setupStep(SetupStep.getInitial())
                .build();

        userRepository.save(user);
        planSubscriptionService.assignDefaultPlanToUser(user);
        setStreamChatToken(user);
        return user;
    }

    private void setStreamChatToken(User user) {
        try {
            upsert().user(io.getstream.chat.java.models.User.UserRequestObject.builder()
                            .id(user.getId().toString())
                            .name(user.getUsername())
                            .additionalField("email", user.getEmail())
                            .build())
                    .request();

            String token = createToken(String.valueOf(user.getId()), null, null);
            user.setStreamChatToken(token);
            userRepository.save(user);
        } catch (StreamException e) {
            throw new StreamIntegrationException(
                    "Error while setting Stream Chat token for user with id: " + user.getId(), e);
        }
    }
}
