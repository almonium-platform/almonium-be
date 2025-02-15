package com.almonium.user.core.factory;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.infra.chat.service.StreamChatService;
import com.almonium.subscription.service.PlanSubscriptionService;
import com.almonium.user.core.model.entity.User;
import com.almonium.user.core.model.enums.SetupStep;
import com.almonium.user.core.repository.UserRepository;
import com.almonium.user.core.service.UsernameGenerator;
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
    StreamChatService streamChatService;

    public User createUserWithDefaultPlan(String email, boolean emailVerified) {
        String username = usernameGenerator.generateUsername(email);

        User user = User.builder()
                .email(email)
                .username(username)
                .emailVerified(emailVerified)
                .planSubscriptions(new HashSet<>())
                .setupStep(SetupStep.getInitial())
                .build();

        userRepository.save(user); // we need to save the user to get the id
        planSubscriptionService.assignDefaultPlanToUser(user);
        streamChatService.createSelfChat(user);

        String token = streamChatService.setStreamChatToken(user);
        user.setStreamChatToken(token);

        return userRepository.save(user);
    }
}
