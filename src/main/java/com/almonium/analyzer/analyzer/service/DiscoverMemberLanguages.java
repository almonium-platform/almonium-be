package com.almonium.analyzer.analyzer.service;

import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.auth.common.service.CurrentMember;
import com.almonium.user.core.repository.LearnerRepository;
import com.almonium.user.core.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Reads the signed-in member's active target and fluent languages when a Discover lookup misses the language asked for.
 * Never called on a hit, and degrades to {@link MemberLanguages#GUEST} on error so a public lookup never fails on
 * profile state.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DiscoverMemberLanguages {
    public record MemberLanguages(boolean signedIn, List<Language> activeTargets, Set<Language> fluent) {
        public static final MemberLanguages GUEST = new MemberLanguages(false, List.of(), Set.of());

        public MemberLanguages {
            activeTargets = List.copyOf(activeTargets);
            fluent = Set.copyOf(fluent);
        }
    }

    private final CurrentMember currentMember;
    private final LearnerRepository learnerRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public MemberLanguages current() {
        try {
            Optional<UUID> memberId = currentMember.id();
            if (memberId.isEmpty()) {
                return MemberLanguages.GUEST;
            }
            UUID id = memberId.get();
            return new MemberLanguages(
                    true, learnerRepository.findActiveLanguagesByUserId(id), userRepository.findFluentLangsById(id));
        } catch (RuntimeException exception) {
            log.warn("Discover member languages unavailable: {}", exception.getMessage());
            return MemberLanguages.GUEST;
        }
    }
}
