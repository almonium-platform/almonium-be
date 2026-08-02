package com.almonium.subscription.service;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.subscription.dto.response.FoundingMemberStatusDto;
import com.almonium.subscription.model.entity.FoundingMember;
import com.almonium.subscription.repository.FoundingMemberRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class FoundingMemberService {
    public static final int CAPACITY = 100;

    FoundingMemberRepository foundingMemberRepository;

    @Transactional(readOnly = true)
    public FoundingMemberStatusDto status() {
        long claimed = foundingMemberRepository.countByStatusIn(
                List.of(FoundingMember.Status.RESERVED, FoundingMember.Status.CONFIRMED));
        return new FoundingMemberStatusDto(CAPACITY, claimed);
    }
}
