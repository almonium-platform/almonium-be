package com.almonium.user.core.service;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.user.core.dto.response.InterestDto;
import com.almonium.user.core.mapper.InterestMapper;
import com.almonium.user.core.repository.InterestRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class InterestService {
    InterestRepository interestRepository;
    InterestMapper interestMapper;

    public List<InterestDto> getInterests() {
        return interestMapper.toDto(interestRepository.findAll());
    }
}
